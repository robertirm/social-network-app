package socialNetwork.repository.file;

import socialNetwork.domain.Entity;
import socialNetwork.domain.User;
import socialNetwork.domain.validator.Validator;
import socialNetwork.repository.memory.UserRepositoryMemory;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class UserRepositoryFileAbstract<ID, E extends Entity<ID>> extends UserRepositoryMemory<ID, E> {
    String filename;

    public UserRepositoryFileAbstract(String filename, Validator<E> validator) {
        super(validator);
        this.filename = filename;
        loadData();
    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> attributes = Arrays.asList(line.split(";"));
                E user = extractUser(attributes);
                super.addUser(user);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract E extractUser(List<String> attributes);

    protected abstract String createUserAsString(E userEntity);

    @Override
    public E addUser(E user) {
        E e = super.addUser(user);
//        if (e == null) {
//            writeToFile(user);
//        }
        return e;
    }

//    protected void writeToFile(E user){
//        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))){
//            bw.write(createUserAsString(user));
//            bw.newLine();
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }

    @Override
    public void writeToFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))){
            for(E user : super.getAllUsers()) {
                bw.write(createUserAsString(user));
                bw.newLine();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
