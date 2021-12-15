<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>

<body>
<div class="container">
    <h2>Genres</h2>
    <!--Search Form -->
    <form action="/genre" method="get" id="searchGenreForm" role="form">
        <input type="hidden" id="searchAction" name="searchAction" value="searchByName">
        <div class="form-group col-xs-5">
            <input type="text" name="genreName" id="genreName" class="form-control" required="true"
                   placeholder="Type the Name of genre"/>
        </div>
        <button type="submit" class="btn btn-info">
            <span class="glyphicon glyphicon-search"></span> Search
        </button>
        <br></br>
        <br></br>
    </form>

    <!--Genres List-->
    <c:if test="${not empty message}">
        <div class="alert alert-success">
                ${message}
        </div>
    </c:if>
    <form action="/genre" method="post" id="genreForm" role="form">
        <input type="hidden" id="name" name="name">
        <input type="hidden" id="action" name="action">
        <c:choose>
            <c:when test="${not empty genreList}">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <td>#</td>
                        <td>Name</td>
                        <td></td>
                    </tr>
                    </thead>
                    <c:forEach var="genre" items="${genreList}">
                        <c:set var="classSuccess" value=""/>
                        <c:if test="${name == genre.name}">
                            <c:set var="classSucess" value="info"/>
                        </c:if>
                        <tr class="${classSuccess}">
                            <td>
                                <a href="/genre?idGenre=${genre.id}&searchAction=searchById&operation=edit">${genre.id}</a>
                            </td>
                            <td>${genre.name}</td>
                            <td>
                                <button class="btn btn-danger btn-md" href="#" id="remove"
                                        onclick="document.getElementById('action').value = 'remove';document.getElementById('name').value = '${genre.name}';
                                                document.getElementById('genreForm').submit();">
                                    Remove
                                    <span class="glyphicon glyphicon-trash"/>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <br>
                <div class="alert alert-info">
                    No genres found matching your search criteria
                </div>
            </c:otherwise>
        </c:choose>
    </form>
    <form action="jsp/genre-jsp/new-genre.jsp">
        <button type="submit" class="btn btn-primary btn-md">New genre</button>
    </form>
    <form action="/film">
        <button type="submit" class="btn btn-check btn-md">To films list</button>
    </form>
</div>
</body>
</html>
