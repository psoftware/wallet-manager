import com.thoughtworks.xstream.converters.basic.*;
import java.time.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Antonio Le Caldare
 */
public class LocalDateConverter extends AbstractSingleValueConverter {  // (01)

    public boolean canConvert(Class type) {
        return (type != null) && LocalDate.class.getPackage().equals(type.getPackage());
    }

    public String toString(Object source) {
        return source.toString();
    }

    public Object fromString(String str) {
        try {
            return LocalDate.parse(str);
        } catch (Exception e) {
            System.out.println("errore: parsing di un campo LocalTime fallito");
            return null;
        }
    }

}

// (01) La classe è stata prelevata da:
//      https://groups.google.com/forum/#!msg/axonframework/TUMYBYBcdMc/mkSASIWtAlYJ