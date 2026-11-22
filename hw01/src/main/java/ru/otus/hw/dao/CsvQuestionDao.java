package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    public static final String ERROR_READING_QUESTIONS = "Error reading questions";

    public static final String FILE_NOT_FOUND_IN_RESOURCES = "File not found in resources: ";

    public static final char SEPARATOR = ';';

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream == null) {
                throw new QuestionReadException(FILE_NOT_FOUND_IN_RESOURCES + fileName);
            }

            List<QuestionDto> questionDtoList = new CsvToBeanBuilder<QuestionDto>(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .withType(QuestionDto.class)
                    .withSeparator(SEPARATOR)
                    .withSkipLines(1)
                    .build()
                    .parse();

            return questionDtoList.stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();

        } catch (Exception e) {
            throw new QuestionReadException(ERROR_READING_QUESTIONS, e);
        }
    }
}
