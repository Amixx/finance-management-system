package edu.lu.financemanagementsystem.controller;

import edu.lu.financemanagementsystem.model.ExpenseCategory;
import edu.lu.financemanagementsystem.model.User;
import edu.lu.financemanagementsystem.repository.ExpenseCategoryRepository;
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
@RequestMapping("/expense-categories")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getAllExpenseCategories(Model model, @RequestParam(defaultValue = "0") int page) throws Exception {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        Pageable pageable = PageRequest.of(page, 10, Sort.by("title").ascending());
        Page<ExpenseCategory> expenseCategoryPage = expenseCategoryRepository.findAllByAuthorId(currentUserId, pageable);
        model.addAttribute("expenseCategories", expenseCategoryPage.getContent());
        model.addAttribute("currentPage", expenseCategoryPage.getNumber());
        return "expense-category/index";
    }

    @GetMapping("/{id}")
    public String getExpenseCategoryById(@PathVariable Long id, Model model) {
        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expenseCategory Id:" + id));
        model.addAttribute("expenseCategory", expenseCategory);
        return "expense-category/view";
    }

    @GetMapping("/new")
    public String showCreationForm(ExpenseCategory expenseCategory) {
        return "expense-category/add";
    }

    @PostMapping("/add")
    public String addExpenseCategory(@Valid ExpenseCategory expenseCategory, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "expense-category/add";
        }
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");
        expenseCategory.setAuthorId(currentUserId);

        expenseCategoryRepository.save(expenseCategory);
        return "redirect:/expense-categories";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expenseCategory Id:" + id));
        model.addAttribute("expenseCategory", expenseCategory);
        return "expense-category/update";
    }

    @PostMapping("/update/{id}")
    public String updateExpenseCategory(@PathVariable Long id, @Valid ExpenseCategory expenseCategory, BindingResult result) {
        if (result.hasErrors()) {
            return "expense-category/update";
        }
        expenseCategoryRepository.save(expenseCategory);
        return "redirect:/expense-categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpenseCategory(@PathVariable Long id) {
        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expenseCategory Id:" + id));
        expenseCategoryRepository.delete(expenseCategory);
        return "redirect:/expense-categories";
    }
}
