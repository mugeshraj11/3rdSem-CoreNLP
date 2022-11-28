package com.nerapplication.NERapplication.controller;

import com.nerapplication.NERapplication.model.Type;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/api/v1")
public class NERController {

    @Autowired
    private StanfordCoreNLP stanfordCoreNLP;

    @PostMapping
    @RequestMapping(value = "/ner")
    public Set<String> ner(@RequestBody final String input, @RequestParam final Type type) {
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        //coreDocument.tokens();

        List<CoreLabel> coreLabels = coreDocument.tokens();
        return new HashSet<>(collectList(coreLabels, type));
    }

    private List<String> collectList(List<CoreLabel> coreLabels, final Type type) {
        return coreLabels
                .stream()
                .filter(coreLabel -> type.getName().equalsIgnoreCase(coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class)))
                .map(CoreLabel::originalText)
                .collect(Collectors.toList());
    }

    @PostMapping
    @RequestMapping(value = "/sentence")
    public HashSet<String> sentence1(@RequestBody final String input) {
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreSentence> sentences = coreDocument.sentences();
        return new HashSet<>(collectLi(sentences));


    }

    private List<String> collectLi(List<CoreSentence> sentences) {
        return sentences
                .stream()
                .map(CoreSentence::toString)
                .collect(Collectors.toList());

    }

    @PostMapping
    @RequestMapping(value = "/sentiment")
    public List<String> sentiment_analysis(@RequestBody final String input) {
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreSentence> sentences = coreDocument.sentences();
        List<String> a = new ArrayList<>();

        for (CoreSentence sentence1 : sentences) {
            String sentiment = sentence1.sentiment();
            a.add(sentiment);
        }
        return a;
    }


    @PostMapping
    @RequestMapping(value="/lemma")
    public List<String> lemmatizer(@RequestBody final String input){
        String lemma;
        List<String> s= new ArrayList<>();
        CoreDocument coreDocument=new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        for(CoreLabel tok:coreDocument.tokens()){
            lemma=tok.lemma();
            s.add(lemma);
        }

        return s;

    }

    @PostMapping
    @RequestMapping(value = "/pos")
    public List<String> pos(@RequestBody final String input){
        List<String> s=new ArrayList<>();
        CoreDocument coreDocument=new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabels=coreDocument.tokens();
        for(CoreLabel coreLabel: coreLabels){
            s.add(coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class));
        }
        return s;
    }
}
