package edu.lu.financemanagementsystem.controller;

import edu.lu.financemanagementsystem.model.Expense;
import edu.lu.financemanagementsystem.model.User;
import edu.lu.financemanagementsystem.repository.ExpenseCategoryRepository;
import edu.lu.financemanagementsystem.repository.ExpenseRepository;
import edu.lu.financemanagementsystem.repository.StoreRepository;
import edu.lu.financemanagementsystem.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;

    @GetMapping
    public String getAllExpenses(Model model, @RequestParam(defaultValue = "0") int page) throws Exception {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        Pageable pageable = PageRequest.of(page, 10, Sort.by("title").ascending());
        Page<Expense> expensePage = expenseRepository.findAllByUserId(currentUserId, pageable);
        model.addAttribute("expenses", expensePage.getContent());
        model.addAttribute("currentPage", expensePage.getNumber());
        return "expense/index";
    }

    @GetMapping("/{id}")
    public String getExpenseById(@PathVariable Long id, Model model) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
        model.addAttribute("expense", expense);
        return "expense/view";
    }

    @GetMapping("/new")
    public String showCreationForm(Expense expense, Model model) throws Exception {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        var userStores = storeRepository.findAllByUserId(currentUserId);
        model.addAttribute("userStores", userStores);

        var expenseCategories = expenseCategoryRepository.findAllByAuthorIdIsNull();
        var userExpenseCategories = expenseCategoryRepository.findAllByAuthorId(currentUserId);
        expenseCategories.addAll(userExpenseCategories);
        model.addAttribute("expenseCategories", expenseCategories);

        return "expense/add";
    }

    @PostMapping("/add")
    public String addExpense(@Valid Expense expense, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "expense/add";
        }
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");
        expense.setUserId(currentUserId);

        expenseRepository.save(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) throws Exception {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
        model.addAttribute("expense", expense);

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        var userStores = storeRepository.findAllByUserId(currentUserId);
        model.addAttribute("userStores", userStores);

        var expenseCategories = expenseCategoryRepository.findAllByAuthorIdIsNull();
        var userExpenseCategories = expenseCategoryRepository.findAllByAuthorId(currentUserId);
        expenseCategories.addAll(userExpenseCategories);
        model.addAttribute("expenseCategories", expenseCategories);

        return "expense/update";
    }

    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable Long id, @Valid Expense expense, BindingResult result) {
        if (result.hasErrors()) {
            return "expense/update";
        }
        expenseRepository.save(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
        expenseRepository.delete(expense);
        return "redirect:/expenses";
    }
}
