package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.domain.Mood;
import com.kulygin.musiccloud.dto.AllMoodsDTO;
import com.kulygin.musiccloud.dto.ErrorResponseBody;
import com.kulygin.musiccloud.dto.MoodDTO;
import com.kulygin.musiccloud.exception.MoodHasExistsException;
import com.kulygin.musiccloud.exception.MoodIsNotExistsException;
import com.kulygin.musiccloud.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/mood")
public class MoodController {

    @Autowired
    private MoodService moodService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMood(@PathVariable("id") Long moodId) {
        Mood mood = moodService.getMoodById(moodId);
        if (mood == null) {
            return getErrorResponseBody(ApplicationErrorTypes.MOOD_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(convert(mood), HttpStatus.OK);
    }

    @RequestMapping(value = "/findAllPagination", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMoodsPagination(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize) {
        Page<Mood> moods = moodService.getAllMoodsPagination(PageRequest.of(page.intValue(), pageSize.intValue(), new Sort(Sort.Direction.ASC, "id")));
        List<Mood> resultListOfMoods = moods.getContent();
        if (resultListOfMoods.size() == 0) {
            return getErrorResponseBody(ApplicationErrorTypes.DB_IS_EMPTY_OR_PAGE_IS_NOT_EXIST);
        }
        return new ResponseEntity<>(convert(resultListOfMoods), HttpStatus.OK);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMoods() {
        List<Mood> moods = moodService.findAll();
        if (moods.size() == 0) {
            return getErrorResponseBody(ApplicationErrorTypes.DB_IS_EMPTY_OR_PAGE_IS_NOT_EXIST);
        }
        return new ResponseEntity<>(convert(moods), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMood(@PathVariable("id") Long moodId) {
        try {
            moodService.deleteMoodById(moodId);
        } catch (MoodIsNotExistsException moodIsNotExists) {
            return getErrorResponseBody(ApplicationErrorTypes.MOOD_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createMood(@RequestParam("name") String name) {
        Mood mood = null;
        try {
            mood = moodService.createMood(name);
        } catch (MoodHasExistsException moodHasExists) {
            return getErrorResponseBody(ApplicationErrorTypes.MOOD_HAS_EXISTS);
        }
        return new ResponseEntity<>(convert(mood), HttpStatus.OK);
    }

    private MoodDTO convert(Mood dbModel) {
        return (dbModel == null) ? null : new MoodDTO(dbModel);
    }

    private AllMoodsDTO convert(List<Mood> dbModel) {
        return (dbModel == null) ? null : new AllMoodsDTO(dbModel);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }

}
