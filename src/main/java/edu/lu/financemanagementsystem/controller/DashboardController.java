package edu.lu.financemanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.lu.financemanagementsystem.model.Expense;
import edu.lu.financemanagementsystem.repository.ExpenseCategoryRepository;
import edu.lu.financemanagementsystem.repository.ExpenseRepository;
import edu.lu.financemanagementsystem.repository.StoreRepository;
import edu.lu.financemanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String getDashboard(
            Model model,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) throws Exception {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(currentUserEmail).orElse(null);
        if (user == null) throw new Exception("User not found");

        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        if (startDate == null || endDate == null) {
            startDateTime = LocalDateTime.now().withDayOfMonth(1);
            endDateTime = LocalDateTime.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(
                user.getId(),
                startDateTime,
                endDateTime);

        Map<String, Long> expenseByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getExpenseCategory().getTitle(),
                        Collectors.summingLong(Expense::getAmount)));

        var expenseByCategoryData = expenseByCategory.entrySet().stream()
                .map(e -> List.of(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        String expenseByCategoryDataJson = objectMapper.writeValueAsString(expenseByCategoryData);

        model.addAttribute("expenseByCategoryDataJson", expenseByCategoryDataJson);

        return "dashboard/index";
    }
}
