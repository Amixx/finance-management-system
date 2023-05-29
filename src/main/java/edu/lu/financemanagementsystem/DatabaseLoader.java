package edu.lu.financemanagementsystem;

import edu.lu.financemanagementsystem.model.*;
import edu.lu.financemanagementsystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final IncomeRepository incomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseLoader(
            UserRepository userRepository,
            StoreRepository storeRepository,
            ExpenseRepository expenseRepository,
            ExpenseCategoryRepository expenseCategoryRepository,
            IncomeRepository incomeRepository,
            IncomeCategoryRepository incomeCategoryRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.incomeRepository = incomeRepository;
        this.incomeCategoryRepository = incomeCategoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... strings) {
        var user = loadUsers();
        if (user.isEmpty()) return;

        var store = loadStores(user.get());
        if (store.isEmpty()) return;

        var expenseCategories = loadExpenseCategories(user.get());
        if (expenseCategories == null) return;

        loadExpenses(user.get(), store.get(), expenseCategories);

        var incomeCategories = loadIncomeCategories(user.get());
        if (incomeCategories == null) return;

        loadIncomes(user.get(), incomeCategories);
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

    private Optional<Store> loadStores(User user) {
        String name = "Rimi Mols";

        if (this.storeRepository.findByName(name).isEmpty()) {
            var store = new Store();
            store.setUserId(user.getId());
            store.setName(name);
            store.setDeleted(false);
            store.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            store.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            this.storeRepository.save(store);
        }

        return this.storeRepository.findByName(name);
    }

    private List<ExpenseCategory> loadExpenseCategories(User user) {
        var expenseCategoryTitle = "Uncategorized";
        if (this.expenseCategoryRepository.findByTitle(expenseCategoryTitle).isPresent()) return null;

        List<ExpenseCategory> expenseCategories = new ArrayList<>();

        for (var color : ExpenseCategory.CategoryColors.values()) {
            var expenseCategory = new ExpenseCategory();
            expenseCategory.setTitle(color.name());
            expenseCategory.setColor(color.getValue());
            expenseCategory.setDeleted(false);
            expenseCategory.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            expenseCategory.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            var saved = this.expenseCategoryRepository.save(expenseCategory);

            if (expenseCategories.isEmpty()) {
                expenseCategories.add(saved);
            }
        }

        var customExpenseCategory = new ExpenseCategory();
        customExpenseCategory.setAuthorId(user.getId());
        customExpenseCategory.setTitle("Custom category");
        customExpenseCategory.setColor("#c08061");
        customExpenseCategory.setDeleted(false);
        customExpenseCategory.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        customExpenseCategory.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        expenseCategories.add(this.expenseCategoryRepository.save(customExpenseCategory));

        return expenseCategories;
    }

    private List<IncomeCategory> loadIncomeCategories(User user) {
        var incomeCategoryTitle = "Uncategorized";
        if (this.incomeCategoryRepository.findByTitle(incomeCategoryTitle).isPresent()) return null;

        List<IncomeCategory> expenseCategories = new ArrayList<>();

        for (var color : IncomeCategory.CategoryColors.values()) {
            var incomeCategory = new IncomeCategory();
            incomeCategory.setTitle(color.name());
            incomeCategory.setColor(color.getValue());
            incomeCategory.setDeleted(false);
            incomeCategory.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            incomeCategory.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            var saved = this.incomeCategoryRepository.save(incomeCategory);

            if (expenseCategories.isEmpty()) {
                expenseCategories.add(saved);
            }
        }

        var customIncomeCategory = new IncomeCategory();
        customIncomeCategory.setAuthorId(user.getId());
        customIncomeCategory.setTitle("Custom category");
        customIncomeCategory.setColor("#c08061");
        customIncomeCategory.setDeleted(false);
        customIncomeCategory.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        customIncomeCategory.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        expenseCategories.add(this.incomeCategoryRepository.save(customIncomeCategory));

        return expenseCategories;
    }

    private void loadExpenses(
            User user,
            Store store,
            List<ExpenseCategory> expenseCategories
    ) {
        var expense1 = new Expense();
        expense1.setUserId(user.getId());
        expense1.setStoreId(store.getId());
        expense1.setExpenseCategoryId(expenseCategories.get(0).getId());
        expense1.setTitle("Test expense");
        expense1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed non risus. Suspendisse lectus tortor, dignissim sit amet, " +
                "adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. ");
        expense1.setAmount(100L);
        expense1.setExpenseDate(LocalDateTime.now());
        expense1.setDeleted(false);
        expense1.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        expense1.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        var expense2 = new Expense();
        expense2.setUserId(user.getId());
        expense2.setStoreId(store.getId());
        expense2.setExpenseCategoryId(expenseCategories.get(1).getId());
        expense2.setTitle("Test expense 2");
        expense2.setAmount(50L);
        expense2.setExpenseDate(LocalDateTime.now());
        expense2.setDeleted(false);
        expense2.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        expense2.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        this.expenseRepository.save(expense1);
        this.expenseRepository.save(expense2);
    }

    private void loadIncomes(
            User user,
            List<IncomeCategory> incomeCategories
    ) {
        var expense1 = new Income();
        expense1.setUserId(user.getId());
        expense1.setIncomeCategoryId(incomeCategories.get(0).getId());
        expense1.setTitle("Test income");
        expense1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed non risus. Suspendisse lectus tortor, dignissim sit amet, " +
                "adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. ");
        expense1.setAmount(100L);
        expense1.setIncomeDate(LocalDateTime.now());
        expense1.setDeleted(false);
        expense1.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        expense1.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        var expense2 = new Income();
        expense2.setUserId(user.getId());
        expense2.setIncomeCategoryId(incomeCategories.get(1).getId());
        expense2.setTitle("Test income 2");
        expense2.setAmount(50L);
        expense2.setIncomeDate(LocalDateTime.now());
        expense2.setDeleted(false);
        expense2.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        expense2.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        this.incomeRepository.save(expense1);
        this.incomeRepository.save(expense2);
    }
}