package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Дао для чтения вопросов из CSV")
class CsvQuestionDaoTest {

    public static final String TEST_QUESTION_1 = "Test question 1?";

    public static final String ANSWER_1 = "Answer 1";

    public static final String ANSWER_2 = "Answer 2";

    public static final String TEST_QUESTION_2 = "Test question 2?";

    public static final String TEST_FILE = "test-questions.csv";

    @Test
    @DisplayName("должен успешно читать вопросы из тестового файла ресурсов")
    void shouldReadQuestionsFromTestFile() {
        TestFileNameProvider fileNameProvider = mock(TestFileNameProvider.class);

        when(fileNameProvider.getTestFileName()).thenReturn(TEST_FILE);

        CsvQuestionDao questionDao = new CsvQuestionDao(fileNameProvider);

        List<Question> questions = questionDao.findAll();

        assertThat(questions).hasSize(2);

        Question firstQuestion = questions.get(0);
        
        assertThat(firstQuestion.text()).isEqualTo(TEST_QUESTION_1);
        assertThat(firstQuestion.answers()).hasSize(2);
        assertThat(firstQuestion.answers().get(0).text()).isEqualTo(ANSWER_1);
        assertThat(firstQuestion.answers().get(0).isCorrect()).isTrue();
        assertThat(firstQuestion.answers().get(1).text()).isEqualTo(ANSWER_2);
        assertThat(firstQuestion.answers().get(1).isCorrect()).isFalse();
        
        Question secondQuestion = questions.get(1);
        assertThat(secondQuestion.text()).isEqualTo(TEST_QUESTION_2);
        assertThat(secondQuestion.answers()).hasSize(3);
    }
}
