package com.kimsongnam.mindset.service;

import com.kimsongnam.mindset.dto.request.AddMoodRequest;
import com.kimsongnam.mindset.entity.mood.Mood;
import com.kimsongnam.mindset.entity.mood.MoodCategory;
import com.kimsongnam.mindset.entity.mood.MoodSituation;
import com.kimsongnam.mindset.entity.mood.repository.MoodRepository;
import com.kimsongnam.mindset.entity.user.User;
import com.kimsongnam.mindset.entity.user.repository.UserRepository;
import com.kimsongnam.mindset.exception.BadRequesetException;
import com.kimsongnam.mindset.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class MoodService {
    private final MoodRepository moodRepository;
    private final UserRepository userRepository;

    @Transactional
    public void AddMood(AddMoodRequest addMoodRequest, BindingResult bindingResult){
        formValidation(bindingResult);
        User user = userRepository.findById(addMoodRequest.getUserId()).orElseThrow(()-> new NotFoundException("존재하지 않는 회원입니다."));
        MoodCategory category = enumCategoryValid(addMoodRequest.getMoodCategory());
        MoodSituation situation = enumSituationValid(addMoodRequest.getMoodSituation());

        Mood mood = Mood.builder()
                        .moodCategory(category)
                        .moodSituation(situation)
                        .moodTitle(addMoodRequest.getMoodTitle())
                        .moodReason(addMoodRequest.getMoodReason())
                        .moodDate(addMoodRequest.getMoodDate())
                        .userId(user)
                .build();

        moodRepository.save(mood);
    }

    public void formValidation(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequesetException("유효하지 않은 형식의 값입니다.");
        }
    }

    public MoodCategory enumCategoryValid(String category) {
        try {
            return MoodCategory.valueOf(category);
        } catch (Exception e) {
            throw new NotFoundException("존재하지 않는 기분입니다.");
        }
    }

    public MoodSituation enumSituationValid(String moodSituation) {
        try {
            return MoodSituation.valueOf(moodSituation);
        } catch (Exception e) {
            throw new NotFoundException("존재하지 않는 형식의 상황입니다.");
        }
    }
}
