package com.company.ApiModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class User
{
    private int id;
    private String userName;
    private String registeredNames;
    
    @Override
    public String toString()
    {
        return "id=" + id +
               ",   Nazwa użytkownika='" + userName + '\'' +
               ",   Dane podane przy rejestracji='" + registeredNames;
    }
}
