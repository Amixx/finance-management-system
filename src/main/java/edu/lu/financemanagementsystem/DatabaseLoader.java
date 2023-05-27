package edu.lu.financemanagementsystem;

import edu.lu.financemanagementsystem.model.Expense;
import edu.lu.financemanagementsystem.model.Store;
import edu.lu.financemanagementsystem.model.User;
import edu.lu.financemanagementsystem.repository.ExpenseRepository;
import edu.lu.financemanagementsystem.repository.StoreRepository;
import edu.lu.financemanagementsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ExpenseRepository expenseRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseLoader(
            UserRepository userRepository,
            StoreRepository storeRepository,
            ExpenseRepository expenseRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.expenseRepository = expenseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... strings) {
        var user = loadUsers();
        var store = loadStores();
        if (user.isPresent() && store.isPresent()) {
            loadExpenses(user.get(), store.get());
        }
    }

    private Optional<User> loadUsers() {
        String email = "admin@fms.lv";

        // Check if the user already exists
        if (this.userRepository.findByEmail(email).isEmpty()) {
            // If not, create the user
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Tester");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("secret"));
            user.setDeleted(false);
            user.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            user.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            // Save the user
            this.userRepository.save(user);
        }

        return this.userRepository.findByEmail(email);
    }

    private Optional<Store> loadStores() {
        String name = "Rimi Mols";

        if (this.storeRepository.findByName(name).isEmpty()) {
            var store = new Store();
            store.setName(name);
            store.setDeleted(false);
            store.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            store.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            this.storeRepository.save(store);
        }

        return this.storeRepository.findByName(name);
    }

    private void loadExpenses(User user, Store store) {
        var expense1 = new Expense();
        expense1.setUserId(user.getId());
        expense1.setStoreId(store.getId());
        expense1.setTitle("Test expense");
        expense1.setDescription("Test expense description");
        expense1.setAmount(100L);
        expense1.setExpense_date(LocalDateTime.now());
        expense1.setDeleted(false);
        expense1.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        expense1.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        var expense2 = new Expense();
        expense2.setUserId(user.getId());
        expense2.setStoreId(store.getId());
        expense2.setTitle("Test expense 2");
        expense2.setDescription("Test expense description 2");
        expense2.setAmount(50L);
        expense2.setExpense_date(LocalDateTime.now());
        expense2.setDeleted(false);
        expense2.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        expense2.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        this.expenseRepository.save(expense1);
        this.expenseRepository.save(expense2);
    }
}