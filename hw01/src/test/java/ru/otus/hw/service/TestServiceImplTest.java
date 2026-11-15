package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования")
class TestServiceImplTest {
    public static final String TEST_ANSWER = "Yes";

    public static final String TEST_QUESTION = "This is a test?";

    public static final String PLEASE_ANSWER_THE_QUESTIONS_BELOW_N = "Please answer the questions below%n";

    private IOService ioService;

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    @DisplayName("должен корректно выводить вопросы и ответы")
    void shouldPrintQuestionsAndAnswers() {
        Question question = new Question(TEST_QUESTION, List.of(new Answer(TEST_ANSWER, true)));

        when(questionDao.findAll()).thenReturn(List.of(question));

        testService.executeTest();

        verify(ioService, times(3)).printLine("");
        verify(ioService).printFormattedLine(PLEASE_ANSWER_THE_QUESTIONS_BELOW_N);
        verify(ioService).printFormattedLine("%d. %s", 1, TEST_QUESTION);
        verify(ioService).printLine(TEST_ANSWER);
    }
}
