package com.faridroid.english10k.viewmodel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import android.app.Application;
import android.util.Log;

import com.faridroid.english10k.data.entity.User;
import com.faridroid.english10k.data.repository.UserRepository;
import com.faridroid.english10k.viewmodel.dto.UserDTO;

import java.util.UUID;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<User> userLiveData;
    private MutableLiveData<Boolean> isUserCreated = new MutableLiveData<>();

    public UserViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userLiveData = userRepository.getUserById();
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
            } else {
                isUserCreated.postValue(true); // El usuario ya existe, no es necesario crearlo
            }
        });
    }

    private void createUser() {
        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
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
