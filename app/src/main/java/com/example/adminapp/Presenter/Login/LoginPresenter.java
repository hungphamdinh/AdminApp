package com.example.adminapp.Presenter.Login;

public class LoginPresenter implements  IUserLoginListener {
    private ILoginView loginView;
    private UserLogin mainLogin;
    public LoginPresenter(ILoginView loginView){
        this.loginView=loginView;
        mainLogin=new UserLogin(this);
    }

    public void onLogin(String phone, String password) {
            //User user = new User();
        mainLogin.isValidData(phone,password);

    }


    @Override
    public void onLoginSucess(String status) {
        loginView.setDisplaySuccess(status);
    }

    @Override
    public void onLoginError(String status) {
        loginView.setDisplayError(status);
    }
}
