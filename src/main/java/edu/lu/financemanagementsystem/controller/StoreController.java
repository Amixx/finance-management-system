package edu.lu.financemanagementsystem.controller;

import edu.lu.financemanagementsystem.model.Store;
import edu.lu.financemanagementsystem.repository.StoreRepository;
import edu.lu.financemanagementsystem.service.StoreService;
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
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreRepository storeRepository;

    @GetMapping
    public String getAllStores(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
        Page<Store> storePage = storeRepository.findAll(pageable);
        model.addAttribute("stores", storePage.getContent());
        model.addAttribute("currentPage", storePage.getNumber());
        return "stores";
    }

    @GetMapping("/{id}")
    public String getStoreById(@PathVariable Long id, Model model) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + id));
        model.addAttribute("store", store);
        return "store-detail";
    }

    @GetMapping("/new")
    public String showCreationForm(Store store) {
        return "add-store";
    }

    @PostMapping("/add")
    public String addStore(@Valid Store store, BindingResult result) {
        if (result.hasErrors()) {
            return "add-store";
        }
        storeRepository.save(store);
        return "redirect:/stores";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid store Id:" + id));
        model.addAttribute("store", store);
        return "update-store";
    }

    @PostMapping("/update/{id}")
    public String updateStore(@PathVariable Long id, @Valid Store store, BindingResult result) {
        if (result.hasErrors()) {
            return "update-store";
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
