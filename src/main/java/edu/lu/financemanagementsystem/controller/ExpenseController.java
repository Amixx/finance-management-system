package edu.lu.financemanagementsystem.controller;

import edu.lu.financemanagementsystem.model.Expense;
import edu.lu.financemanagementsystem.repository.ExpenseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping
    public String getAllExpenses(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
        Page<Expense> expensePage = expenseRepository.findAll(pageable);
        model.addAttribute("expenses", expensePage.getContent());
        model.addAttribute("currentPage", expensePage.getNumber());
        return "expenses";
    }

    @GetMapping("/{id}")
    public String getExpenseById(@PathVariable Long id, Model model) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
        model.addAttribute("expense", expense);
        return "expense-detail";
    }

    @GetMapping("/new")
    public String showCreationForm(Expense expense) {
        return "add-expense";
    }

    @PostMapping("/add")
    public String addExpense(@Valid Expense expense, BindingResult result) {
        if (result.hasErrors()) {
            return "add-expense";
        }
        expenseRepository.save(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
        model.addAttribute("expense", expense);
        return "update-expense";
    }

    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable Long id, @Valid Expense expense, BindingResult result) {
        if (result.hasErrors()) {
            return "update-expense";
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
