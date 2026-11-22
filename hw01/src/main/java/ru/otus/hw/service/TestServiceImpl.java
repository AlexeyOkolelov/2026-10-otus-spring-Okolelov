package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    public static final String PLEASE_INPUT_YOUR_ANSWER = "Please input your answer:";

    public static final String INVALID_INPUT_PLEASE_ENTER_A_NUMBER_BETWEEN =
            "Invalid input. Please enter a number between 1 and ";

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine("");
            ioService.printLine(question.text());
            List<Answer> answers = question.answers();
            for (int i = 0; i < answers.size(); i++) {
                ioService.printFormattedLine("%d. %s", i + 1, answers.get(i).text());
            }
            ioService.printLine("");
            int userAnswerIndex = ioService.readIntForRangeWithPrompt(
                    1,
                    answers.size(),
                    PLEASE_INPUT_YOUR_ANSWER,
                    INVALID_INPUT_PLEASE_ENTER_A_NUMBER_BETWEEN + answers.size()
            );
            var isAnswerValid = answers.get(userAnswerIndex - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}

