package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaRatingService {
    private final MpaRatingDao mpaRatingDao;

    public List<MpaRating> getAll() {
        return mpaRatingDao.findAll();
    }

    public MpaRating getById(int id) {
        return mpaRatingDao.getById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id=" + id + " не найден"));
    }
}
