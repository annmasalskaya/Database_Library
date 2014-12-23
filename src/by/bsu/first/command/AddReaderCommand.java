package by.bsu.first.command;

import by.bsu.first.DAO.ReaderDAO;
import by.bsu.first.exceptions.CommandException;
import by.bsu.first.exceptions.DAOCommandException;
import by.bsu.first.exceptions.LogicException;
import by.bsu.first.logic.AddReaderLogic;
import by.bsu.first.manager.ConfigManager;
import by.bsu.first.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class AddReaderCommand implements Command {

    private static final String PARAM_IDCARD = "idcard";
    private static final String PARAM_LASTNAME = "lastname";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_SECONDNAME = "secondname";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phone";

    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String page = null;
        String idcard = request.getParameter(PARAM_IDCARD);
        String lastname = request.getParameter(PARAM_LASTNAME);
        String name = request.getParameter(PARAM_NAME);
        String secondname = request.getParameter(PARAM_SECONDNAME);
        String address = request.getParameter(PARAM_ADDRESS);
        String phone = request.getParameter(PARAM_PHONE);
        HttpSession session = request.getSession(true);
        String locale = (String) session.getAttribute("locale");
        if (locale == null)
            locale = "ru";

        if (idcard == null || lastname == null || lastname.isEmpty() || name == null || name.isEmpty() || secondname == null || secondname.isEmpty() || address == null || address.isEmpty() || phone == null || phone.isEmpty()) {
            request.setAttribute("errorFillMessage", MessageManager.getMessage("message.fillerror", locale));
            page = ConfigManager.getProperty("path.page.addreader");
        } else {
            int IDcard = Integer.parseInt(idcard);
            try {
                if (!AddReaderLogic.addReader(idcard)) {
                    ReaderDAO dao = new ReaderDAO();
                    try {
                        dao.addReader(IDcard, lastname, name, secondname, address, phone);
                    } catch (DAOCommandException e) {
                        throw new CommandException(e.getCause());
                    }
                    request.setAttribute("successMessage", MessageManager.getMessage("message.borrowerror_success", locale));
                    page = ConfigManager.getProperty("path.page.addreader");

                } else {
                    request.setAttribute("errorMessage", MessageManager.getMessage("message.readerexist", locale));
                    page = ConfigManager.getProperty("path.page.addreader");
                }
            } catch (LogicException e) {
                throw new CommandException(e.getCause());
            }

        }


        return page;
    }
}
