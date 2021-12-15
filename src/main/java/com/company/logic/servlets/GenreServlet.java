package com.company.logic.servlets;

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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(
        name = "GenreServlet",
        urlPatterns = {"/genre"}
)
public class GenreServlet extends HttpServlet {

    MovieStoreDAO movieStoreDAO = new MovieStoreDAO();

    public GenreServlet() throws Exception {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("searchAction");
        if (action != null) {
            switch (action) {
                case "searchById":
                    searchGenreById(req, resp);
                    break;
                case "searchByName":
                    searchGenreByName(req, resp);
                    break;
            }
        } else {
            List<Genre> result = movieStoreDAO.getGenres();
            forwardListGenres(req, resp, result);
        }
    }

    private void searchGenreById(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int idGenre = Integer.parseInt(req.getParameter("idGenre"));
        String operationType = req.getParameter("operation");
        Genre genre = null;
        try {
            genre = movieStoreDAO.getGenreById(idGenre);
        } catch (Exception ex) {
            Logger.getLogger(GenreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("genre", genre);
        req.setAttribute("action", "add");
        String nextJSP = "/jsp/genre-jsp/new-genre.jsp";

        if (Objects.equals(operationType, "edit")) {
            nextJSP = "/jsp/genre-jsp/edit-genre.jsp";
            req.setAttribute("action", "edit");
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }

    private void searchGenreByName(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");

        Genre genre = null;
        try {
            genre = movieStoreDAO.getGenreByName(name);
        } catch (Exception ex) {
            Logger.getLogger(GenreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setAttribute("genre", genre);
        req.setAttribute("action", "edit");
        String nextJSP = "/jsp/genre-jsp/new-genre.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }

    private void forwardListGenres(HttpServletRequest req, HttpServletResponse resp, List genreList)
            throws ServletException, IOException {
        String nextJSP = "/jsp/genre-jsp/list-genres.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("genreList", genreList);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "add":
                addGenreAction(req, resp);
                break;
            case "edit":
                try {
                    editGenreAction(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "remove":
                try {
                    removeGenreById(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void addGenreAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        movieStoreDAO.addGenre(new Genre(name));

        List<Genre> unitList = movieStoreDAO.getGenres();
        req.setAttribute("name", name);
        String message = "The new genre has been successfully created.";
        req.setAttribute("message", message);
        forwardListGenres(req, resp, unitList);
    }

    private void editGenreAction(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String name = req.getParameter("name");

        String newName = req.getParameter("newName");

        boolean success = movieStoreDAO.updateGenre(name, new Genre(newName));

        String message = null;
        if (success) {
            message = "The genre has been successfully updated.";
        }
        List<Genre> genreList = movieStoreDAO.getGenres();
        req.setAttribute("message", message);
        forwardListGenres(req, resp, genreList);
    }

    private void removeGenreById(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String name = req.getParameter("name");
        boolean confirm = movieStoreDAO.deleteGenre(name);

        if (confirm) {
            String message = "The genre has been successfully removed.";
            req.setAttribute("message", message);
        }

        List<Genre> genreList = movieStoreDAO.getGenres();
        forwardListGenres(req, resp, genreList);
    }

}
