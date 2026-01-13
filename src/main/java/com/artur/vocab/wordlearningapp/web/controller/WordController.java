package com.artur.vocab.wordlearningapp.web.controller;

import com.artur.vocab.wordlearningapp.domain.entity.WordEntity;
import com.artur.vocab.wordlearningapp.repo.WordRepository;
import com.artur.vocab.wordlearningapp.service.CategoryService;
import com.artur.vocab.wordlearningapp.service.GroupService;
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
    private final CategoryService categoryService;
    private final GroupService groupService;

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

        if (word.getEnrichment() != null) {
            model.addAttribute("category", categoryService.getCategory(word.getEnrichment().getCategoryId()));
            model.addAttribute("group", groupService.getGroup(word.getEnrichment().getGroupId()));
        }

        return "word-details";
    }

    @PostMapping("/words/{id}/delete")
    public String deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return "redirect:/";
    }
}
