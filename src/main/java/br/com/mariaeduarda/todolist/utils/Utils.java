package br.com.mariaeduarda.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

  // copia tudo que é não nulo
  public static void copyNonNullProperties(Object source, Object target) {

    BeanUtils.copyProperties(source, target, getNullPropertyNames(source));

  }

  public static String[] getNullPropertyNames(Object source) {
    // BeanWrapper - interface para acessar as propriedades de um objeto
    final BeanWrapper src = new BeanWrapperImpl(source);

    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>(); // insere as informações

    for (PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        // coloca todas as propriedades nulas no emptyNames
        emptyNames.add(pd.getName());
      }
    }

    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result); // converte as propriedades em um array de strings
  }
}
