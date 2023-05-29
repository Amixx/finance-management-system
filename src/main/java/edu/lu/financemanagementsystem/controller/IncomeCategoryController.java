package edu.lu.financemanagementsystem.controller;

import edu.lu.financemanagementsystem.model.IncomeCategory;
import edu.lu.financemanagementsystem.model.User;
import edu.lu.financemanagementsystem.repository.IncomeCategoryRepository;
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
@RequestMapping("/income-categories")
public class IncomeCategoryController {

    @Autowired
    private IncomeCategoryRepository incomeCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getAllIncomeCategories(Model model, @RequestParam(defaultValue = "0") int page) throws Exception {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        Pageable pageable = PageRequest.of(page, 10, Sort.by("title").ascending());
        Page<IncomeCategory> incomeCategoryPage = incomeCategoryRepository.findAllByAuthorId(currentUserId, pageable);
        model.addAttribute("incomeCategories", incomeCategoryPage.getContent());
        model.addAttribute("currentPage", incomeCategoryPage.getNumber());
        return "income-category/index";
    }

    @GetMapping("/{id}")
    public String getIncomeCategoryById(@PathVariable Long id, Model model) {
        IncomeCategory incomeCategory = incomeCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid incomeCategory Id:" + id));
        model.addAttribute("incomeCategory", incomeCategory);
        return "income-category/view";
    }

    @GetMapping("/new")
    public String showCreationForm(IncomeCategory incomeCategory) {
        return "income-category/add";
    }

    @PostMapping("/add")
    public String addIncomeCategory(@Valid IncomeCategory incomeCategory, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "income-category/add";
        }
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");
        incomeCategory.setAuthorId(currentUserId);

        incomeCategoryRepository.save(incomeCategory);
        return "redirect:/income-categories";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        IncomeCategory incomeCategory = incomeCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid incomeCategory Id:" + id));
        model.addAttribute("incomeCategory", incomeCategory);
        return "income-category/update";
    }

    @PostMapping("/update/{id}")
    public String updateIncomeCategory(@PathVariable Long id, @Valid IncomeCategory incomeCategory, BindingResult result) {
        if (result.hasErrors()) {
            return "income-category/update";
        }
        incomeCategoryRepository.save(incomeCategory);
        return "redirect:/income-categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteIncomeCategory(@PathVariable Long id) {
        IncomeCategory incomeCategory = incomeCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid incomeCategory Id:" + id));
        incomeCategoryRepository.delete(incomeCategory);
        return "redirect:/income-categories";
    }
}
