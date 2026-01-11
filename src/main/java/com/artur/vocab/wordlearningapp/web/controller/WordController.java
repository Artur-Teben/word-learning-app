package com.artur.vocab.wordlearningapp.web.controller;


import com.artur.vocab.wordlearningapp.repo.WordRepository;
import com.artur.vocab.wordlearningapp.service.WordService;
import com.artur.vocab.wordlearningapp.web.dto.CreateWordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WordController {

    private final WordRepository wordRepository;
    private final WordService wordService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("createWordRequest", new CreateWordRequest());
        model.addAttribute("words", wordRepository.findAllByOrderByCreatedAtDesc());
        return "home";
    }

    @PostMapping("/words")
    public String addWord(@Valid @ModelAttribute("createWordRequest") CreateWordRequest request) {
        wordService.addWord(request);
        return "redirect:/";
    }
}
