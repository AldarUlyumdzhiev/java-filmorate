package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public List<Genre> getAll() {
        return genreDao.findAll();
    }

    public Genre getById(int id) {
        return genreDao.getById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Жанр с id=%d не найден", id)
                ));
    }
}
