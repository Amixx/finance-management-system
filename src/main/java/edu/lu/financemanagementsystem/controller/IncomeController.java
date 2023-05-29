package edu.lu.financemanagementsystem.controller;

import edu.lu.financemanagementsystem.model.Income;
import edu.lu.financemanagementsystem.model.User;
import edu.lu.financemanagementsystem.repository.IncomeCategoryRepository;
import edu.lu.financemanagementsystem.repository.IncomeRepository;
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
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private IncomeCategoryRepository incomeCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;

    @GetMapping
    public String getAllIncomes(Model model, @RequestParam(defaultValue = "0") int page) throws Exception {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        Pageable pageable = PageRequest.of(page, 10, Sort.by("title").ascending());
        Page<Income> incomePage = incomeRepository.findAllByUserId(currentUserId, pageable);
        model.addAttribute("incomes", incomePage.getContent());
        model.addAttribute("currentPage", incomePage.getNumber());
        return "income/index";
    }

    @GetMapping("/{id}")
    public String getIncomeById(@PathVariable Long id, Model model) {
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid income Id:" + id));
        model.addAttribute("income", income);
        return "income/view";
    }

    @GetMapping("/new")
    public String showCreationForm(Income income, Model model) throws Exception {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        var userStores = storeRepository.findAllByUserId(currentUserId);
        model.addAttribute("userStores", userStores);

        var incomeCategories = incomeCategoryRepository.findAllByAuthorIdIsNull();
        var userIncomeCategories = incomeCategoryRepository.findAllByAuthorId(currentUserId);
        incomeCategories.addAll(userIncomeCategories);
        model.addAttribute("incomeCategories", incomeCategories);

        return "income/add";
    }

    @PostMapping("/add")
    public String addIncome(@Valid Income income, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "income/add";
        }
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");
        income.setUserId(currentUserId);

        incomeRepository.save(income);
        return "redirect:/income";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) throws Exception {
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid income Id:" + id));
        model.addAttribute("income", income);

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        var userStores = storeRepository.findAllByUserId(currentUserId);
        model.addAttribute("userStores", userStores);

        var incomeCategories = incomeCategoryRepository.findAllByAuthorIdIsNull();
        var userIncomeCategories = incomeCategoryRepository.findAllByAuthorId(currentUserId);
        incomeCategories.addAll(userIncomeCategories);
        model.addAttribute("incomeCategories", incomeCategories);

        return "income/update";
    }

    @PostMapping("/update/{id}")
    public String updateIncome(@PathVariable Long id, @Valid Income income, BindingResult result) {
        if (result.hasErrors()) {
            return "income/update";
        }
        incomeRepository.save(income);
        return "redirect:/income";
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id) {
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid income Id:" + id));
        incomeRepository.delete(income);
        return "redirect:/income";
    }
}
