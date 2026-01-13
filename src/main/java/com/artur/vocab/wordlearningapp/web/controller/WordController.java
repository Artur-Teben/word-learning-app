package com.artur.vocab.wordlearningapp.web.controller;

import com.artur.vocab.wordlearningapp.domain.entity.CategoryEntity;
import com.artur.vocab.wordlearningapp.domain.entity.WordEntity;
import com.artur.vocab.wordlearningapp.repo.WordRepository;
import com.artur.vocab.wordlearningapp.service.WordService;
import com.artur.vocab.wordlearningapp.web.dto.CreateWordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        WordEntity saved = wordService.addWord(request);
        return "redirect:/words/" + saved.getId();
    }

    @GetMapping("/words/{id}")
    public String getWord(@PathVariable Long id, Model model) {
        WordEntity word = wordService.getWord(id);
        model.addAttribute("word", word);

        if (word.getEnrichment() != null && word.getEnrichment().getCategoryId() != null) {
            CategoryEntity category = wordService.getCategory(word.getEnrichment().getCategoryId());
            model.addAttribute("category", category);
        }

        return "word-details";
    }
}
