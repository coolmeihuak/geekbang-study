package cc.page.study.week9.practice2.rpcfx.provider;


import cc.page.study.week9.practice2.rpcfx.api.User;
import cc.page.study.week9.practice2.rpcfx.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
