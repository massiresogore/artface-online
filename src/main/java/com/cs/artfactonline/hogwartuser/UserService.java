package com.cs.artfactonline.hogwartuser;

import com.cs.artfactonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<HogwartUser> findAll()
    {
        return  this.userRepository.findAll();
    }

    public HogwartUser findById(Integer userId)
    {
        return userRepository.findById(userId).orElseThrow(()->new ObjectNotFoundException("user", userId));
    }

    public HogwartUser save(HogwartUser user)
    {
        //Encodé le mot de passe prochainement...
        return userRepository.save(user);
    }

    public HogwartUser update(Integer userId, HogwartUser update)
    {
        return this.userRepository.findById(userId)
                .map(oldUser->{
                    oldUser.setUsername(update.getUsername());
                    oldUser.setEnable(update.getEnable());
                    oldUser.setRoles(update.getRoles());

                    return this.userRepository.save(oldUser);
                })
                .orElseThrow(()->new ObjectNotFoundException("user",userId));
    }

    public void delete(Integer userId)
    {
         userRepository.findById(userId).orElseThrow(()->new ObjectNotFoundException("user", userId));

         this.userRepository.deleteById(userId);

    }
}
