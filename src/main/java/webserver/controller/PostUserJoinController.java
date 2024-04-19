package webserver.controller;

import db.DataBase;
import model.User;
import webserver.dto.UserDto;
import webserver.exception.ConflictException;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import static webserver.controller.RequestMapper.Entry.DEFAULT_ENTRY;

public final class PostUserJoinController implements Controller {
    @Override
    public void doService(HttpRequest req, HttpResponse res) {
        UserDto userDto = UserDto.from(req.contents());
        User user = User.from(userDto);
        User userOnDataBase = DataBase.findUserById(user.getUserId());
        if (userOnDataBase != null) {
            throw new ConflictException("DUPLICATED");
        }
        DataBase.addUser(user);
        res.setMovedTemporarily(DEFAULT_ENTRY);
    }
}
