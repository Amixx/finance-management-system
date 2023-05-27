package edu.lu.financemanagementsystem.controller;

import edu.lu.financemanagementsystem.model.Store;
import edu.lu.financemanagementsystem.model.User;
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
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getAllStores(Model model, @RequestParam(defaultValue = "0") int page) throws Exception {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserEmail).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");

        Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
        Page<Store> storePage = storeRepository.findAllByUserId(currentUserId, pageable);
        model.addAttribute("stores", storePage.getContent());
        model.addAttribute("currentPage", storePage.getNumber());
        return "store/index";
    }

    @GetMapping("/{id}")
    public String getStoreById(@PathVariable Long id, Model model) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + id));
        model.addAttribute("store", store);
        return "store/view";
    }

    @GetMapping("/new")
    public String showCreationForm(Store store) {
        return "store/add";
    }

    @PostMapping("/add")
    public String addStore(@Valid Store store, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return "store/add";
        }
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUserId = userRepository.findByEmail(currentUserName).map(User::getId).orElse(null);
        if (currentUserId == null) throw new Exception("User not found");
        store.setUserId(currentUserId);
        storeRepository.save(store);
        return "redirect:/stores";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + id));
        model.addAttribute("store", store);
        return "store/update";
    }

    @PostMapping("/update/{id}")
    public String updateStore(@PathVariable Long id, @Valid Store store, BindingResult result) {
        if (result.hasErrors()) {
            return "store/update";
        }
        storeRepository.save(store);
        return "redirect:/stores";
    }

    @GetMapping("/delete/{id}")
    public String deleteStore(@PathVariable Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + id));
        storeRepository.delete(store);
        return "redirect:/stores";
    }
}
