package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below");

        List<Question> questionList = questionDao.findAll();

        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            ioService.printFormattedLine("%d. %s", i + 1, question.text());
            ioService.printLine("");

            List<Answer> answerList = question.answers();
            answerList.forEach(answer -> ioService.printLine(answer.text()));
            ioService.printLine("");
        }
    }
}

