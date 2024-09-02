package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.faridroid.english10k.data.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    // Cambiamos el tipo de dato de 'int' a 'String' para el id
    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<User> getUserById(String id);

    @Query("DELETE FROM users WHERE id = :id")
    void deleteUser(String id);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    // Opcional: Método para obtener el usuario más reciente por fecha de creación
    @Query("SELECT * FROM users ORDER BY createdAt DESC LIMIT 1")
    LiveData<User> getUserByCreatedAt();

    @Query("UPDATE users SET xp = :xp WHERE id = :id")
    void updateXp(String id, long xp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> usersBackup);

    @Query("SELECT * FROM users")
    List<User> getAllUsersDirect();
}
