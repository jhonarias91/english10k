package com.faridroid.english10k.view.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.entity.User;
import com.faridroid.english10k.data.repository.UserRepository;
import com.faridroid.english10k.service.CategoryService;
import com.faridroid.english10k.data.dto.UserDTO;

import java.util.UUID;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<User> userLiveData;
    private MutableLiveData<Boolean> isUserCreated = new MutableLiveData<>();
    private CategoryService categoryService;

    public UserViewModel(Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        userLiveData = userRepository.getUserById();
        categoryService = CategoryService.getInstance(application);
    }

    public LiveData<UserDTO> getUserDTO() {
        return Transformations.map(userLiveData, user -> {
            if (user != null){
                return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), null, user.getXp());
            } else {
                return null;
            }
        });
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void updateXp(String id, long xp) {
        userRepository.updateXp(id, xp);
    }


    public LiveData<Boolean> getIsUserCreated() {
        return isUserCreated;
    }

    public void checkOrCreateUser() {
        userLiveData.observeForever(user -> {
            if (user == null) {
                createUser();
                createDefaultCategory();
            } else {
                isUserCreated.postValue(true); // El usuario ya existe, no es necesario crearlo
            }
        });
    }

    private void createDefaultCategory() {

    }

    private void createUser() {
        User newUser = new User();
        String userId = UUID.randomUUID().toString();
        newUser.setId(userId);
        newUser.setCreatedAt(System.currentTimeMillis());
        newUser.setXp(0); // Asumiendo que tienes un campo de experiencia inicial
        userRepository.insertUser(newUser).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                Log.e("UserViewModel", "Error creating user", throwable);
                isUserCreated.postValue(false);
            } else {
                isUserCreated.postValue(true);
            }
        });
    }
}
