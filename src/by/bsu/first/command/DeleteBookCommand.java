package by.bsu.first.command;

import by.bsu.first.DAO.BookDAO;
import by.bsu.first.entity.Book;
import by.bsu.first.exceptions.CommandException;
import by.bsu.first.exceptions.DAOException;
import by.bsu.first.manager.ConfigManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public class DeleteBookCommand implements Command {

    private static final String PARAM_DELETE = "id";
    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String select[] = request.getParameterValues(PARAM_DELETE);

        if (select != null && select.length != 0) {
           for (int i = 0; i < select.length; i++) {

                BookDAO dao = new BookDAO();
                try {
                    dao.deleteBook(select[i]);
                } catch (DAOException e) {
                    throw new CommandException(e.getCause());
                }
}
        }
        BookDAO dao = new BookDAO();
        List<Book> lst = null;
        try {
            lst = dao.findAll();
        } catch (DAOException e) {
            throw new CommandException(e.getCause());
        }
        request.setAttribute("lst", lst);
        String page = ConfigManager.getProperty("path.page.deletebook");
        return page;
    }
}
