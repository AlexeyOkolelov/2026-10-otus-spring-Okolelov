package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования")
class TestServiceImplTest {

    public static final String TEST_QUESTION = "This is a test?";

    public static final String PLEASE_ANSWER_THE_QUESTIONS_BELOW = "Please answer the questions below";

    public static final String FIRST_NAME = "Ivan";

    public static final String LAST_NAME = "Ivanov";

    public static final String ANSWER_NO = "No";

    public static final String ANSWER_YES = "Yes";

    private IOService ioService;

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    private Student student;

    private Question question;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);

        student = new Student(FIRST_NAME, LAST_NAME);
        Answer wrongAnswer = new Answer(ANSWER_NO, false);
        Answer rightAnswer = new Answer(ANSWER_YES, true);
        question = new Question(TEST_QUESTION, List.of(wrongAnswer, rightAnswer));
    }

    @Test
    @DisplayName("должен корректно выводить вопросы и ответы")
    void shouldPrintQuestionsAndAnswers() {

        when(questionDao.findAll()).thenReturn(List.of(question));

        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(2);

        testService.executeTestFor(student);

        verify(ioService, times(3)).printLine("");
        verify(ioService).printFormattedLine(PLEASE_ANSWER_THE_QUESTIONS_BELOW);
        verify(ioService).printLine(TEST_QUESTION);
        verify(ioService).printFormattedLine("%d. %s", 1, ANSWER_NO);
        verify(ioService).printFormattedLine("%d. %s", 2, ANSWER_YES);
    }

    @Test
    @DisplayName("должен корректно проводить тестирование и считать баллы")
    void shouldExecuteTestAndCalculateResult() {

        when(questionDao.findAll()).thenReturn(List.of(question));

        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(2);

        TestResult result = testService.executeTestFor(student);

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getAnsweredQuestions()).hasSize(1);
        assertThat(result.getRightAnswersCount()).isEqualTo(1);
    }
}
