package socialNetwork.repository.file;

import socialNetwork.domain.Entity;
import socialNetwork.domain.validator.Validator;
import socialNetwork.repository.UserRepository;
import socialNetwork.repository.memory.FriendshipRepositoryMemory;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class FriendshipRepositoryFileAbstract<ID, E extends Entity<ID>> extends FriendshipRepositoryMemory<ID, E> {
    String filename;

    public FriendshipRepositoryFileAbstract(String filename, Validator<E> validator) {
        super(validator);
        this.filename = filename;
        loadData();
    }

    private void loadData(){
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = br.readLine()) != null){
                List<String> attributes = Arrays.asList(line.split(";"));
                E friendship = extractFriendship(attributes);
                super.addFriendship(friendship);
            }

        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public abstract E extractFriendship(List<String> attributes);

    protected abstract String createFriendshipAsString(E friendshipEntity);

    @Override
    public E addFriendship(E friendshipEntity){
        E e = super.addFriendship(friendshipEntity);
//        if(e == null){
//            writeToFile(friendshipEntity);
//        }
        return e;
    }

//    protected void writeToFile(E friendship){
//        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))){
//            bw.write(createFriendshipAsString(friendship));
//            bw.newLine();
//        } catch (FileNotFoundException ex){
//            ex.printStackTrace();
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }
//    }

    @Override
    public void writeToFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))){
            for(E friendship : super.getAllFriendships()) {
                bw.write(createFriendshipAsString(friendship));
                bw.newLine();
            }
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
