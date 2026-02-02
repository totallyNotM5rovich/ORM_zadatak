//Kreirati novi Java projekt koji će koristiti radne okvire Hibernate i JPA i implementirati primjer veze „1:N” s entitetima „Jelo” (engl. Meal) i sastojci (engl. Ingredients)
//Klasa „Meal” mora sadržavati identifikator „Long id” i naziv „String name” te listu objekata klase „Ingredient” te anotaciju „@OneToMany”
//Klasa „Ingredient” mora sadržavati identifikator „Long id” i naziv „String name” i objekt klase „Meal” kojem pripada taj sastojak označen s anotacijama „@ManyToOne” i „@JoinColumn”
//Kreirati i glavnu klasu koja sprema objekt klase „Meal” zajedno s nekoliko sastojaka.
//Napisati upit koji će dohvaćati podatke o svim jelima.

package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.model.Ingredient;
import org.example.model.Meal;

import java.util.List;

public class Main {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("cookbook");
    private static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
        inicijalizacijaBaze();

        List<Meal> jela = em.createQuery("SELECT m FROM Meal m", Meal.class).getResultList();

        for (Meal meal : jela) {
            String sastojci = "";
            for (Ingredient ingredient : meal.getIngredients()) {
                sastojci = sastojci.concat(ingredient.getName().toLowerCase() + ", ");
            }
            System.out.printf("Jelo:\r\n%s\r\nSastojci: %s\r\n\r\n", meal.getName(), sastojci.substring(0,sastojci.length()-2));
        }

//        System.out.println(jela.get(0).getIngredients().toString());
//        System.out.println(jela.get(1).getIngredients().toString());

        em.close();
        emf.close();
    }

    private static void inicijalizacijaBaze() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        String[] sastojciPasticada = {"Govedji but","Slanina","Luk","Cesnjak","Crno Vino"};
        Meal pasticada = new Meal("Pasticada");
        for (String s : sastojciPasticada) {
            pasticada.getIngredients().add(new Ingredient(s));
        }
        em.persist(pasticada);

        String[] sastojciGrah = {"Grah","Slanina","Luk","Slatka paprika","Brasno"};
        Meal zapeceniGrah = new Meal("Zapeceni grah");
        for (String s : sastojciGrah) {
            zapeceniGrah.getIngredients().add(new Ingredient(s));
        }
        em.persist(zapeceniGrah);

        tx.commit();
    }
}