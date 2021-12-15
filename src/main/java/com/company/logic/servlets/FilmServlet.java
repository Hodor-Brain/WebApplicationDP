package com.company.logic.servlets;

import com.company.logic.subjects.Film;
import com.company.logic.subjects.Genre;
import com.company.logic.subjects.MovieStoreDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(
        name = "FilmServlet",
        urlPatterns = {"/film"}
)
public class FilmServlet extends HttpServlet {

    MovieStoreDAO movieStoreDAO = new MovieStoreDAO();

    public FilmServlet() throws Exception {
        movieStoreDAO.deleteAllFilms();
        movieStoreDAO.deleteAllGenres();

        movieStoreDAO.addGenre(new Genre("Comedy"));
        movieStoreDAO.addGenre(new Genre("Horror"));

        movieStoreDAO.addFilm(new Film("Friends", 2f, "Comedy"));
        movieStoreDAO.addFilm(new Film("Harry Potter", 2.5f, "Horror"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("searchAction");
        if (action!=null){
            switch (action) {
                case "searchByName":
                    findFilmByName(req, resp);
                    break;
            }
        }else{
            List<Film> result = movieStoreDAO.getFilms();
            forwardListFilms(req, resp, result);
        }
    }

    private void findFilmByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String filmName = req.getParameter("filmName");
        Film film = null;
        try {
            film = movieStoreDAO.getFilmByName(filmName);
        } catch (Exception ex) {
            Logger.getLogger(FilmServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("film", film);
        req.setAttribute("action", "edit");
        String nextJSP = "/jsp/film-jsp/new-film.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }

    private void forwardListFilms(HttpServletRequest req, HttpServletResponse resp, List filmList)
            throws ServletException, IOException {
        String nextJSP = "/jsp/film-jsp/list-films.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("filmList", filmList);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "add":
                addFilmAction(req, resp);
                break;
            case "edit":
                try {
                    editFilmAction(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "remove":
                removeFilmByName(req, resp);
                break;
        }

    }

    private void addFilmAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        float duration = Float.parseFloat(req.getParameter("duration"));
        String genre = req.getParameter("genre");

        Film film = new Film(name, duration, genre);
        movieStoreDAO.addFilm(film);
        List<Film> filmList = movieStoreDAO.getFilms();
        req.setAttribute("name", name);
        String message = "The new film has been successfully created.";
        req.setAttribute("message", message);
        forwardListFilms(req, resp, filmList);
    }

    private void editFilmAction(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String name = req.getParameter("name");
        float duration = Float.parseFloat(req.getParameter("duration"));
        String genre = req.getParameter("genre");
        Film oldFilm = new Film(name, duration, genre);

        String newName = req.getParameter("newName");
        float newDuration = Float.parseFloat(req.getParameter("newDuration"));
        String newGenre = req.getParameter("newGenre");
        Film newFilm = new Film(newName, newDuration, newGenre);

        boolean success = movieStoreDAO.updateFilm(oldFilm, newFilm);

        String message = null;
        if (success) {
            message = "The film has been successfully updated.";
        }
        List<Film> filmList = movieStoreDAO.getFilms();
        req.setAttribute("message", message);
        forwardListFilms(req, resp, filmList);
    }

    private void removeFilmByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        boolean confirm = movieStoreDAO.deleteFilm(name);

        if (confirm){
            String message = "The film has been successfully removed.";
            req.setAttribute("message", message);
        }

        List<Film> filmList = movieStoreDAO.getFilms();
        forwardListFilms(req, resp, filmList);
    }

}

