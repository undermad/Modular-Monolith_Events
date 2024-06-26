package example.commands.handlers;

import example.commands.CommandHandler;
import example.commands.RemovePackingList;
import example.exceptions.PackingListDoesntExistException;
import example.repository.PackingListRepository;
import example.value_objects.PackingListId;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class RemovePackingListHandler implements CommandHandler<RemovePackingList> {

    private final PackingListRepository repository;

    public RemovePackingListHandler(PackingListRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Void> handleAsync(RemovePackingList command) {
        return CompletableFuture.runAsync(() -> {
            var packingList = repository.getAsync(new PackingListId(command.packingListId())).join();

            if (packingList == null) throw new PackingListDoesntExistException(command.packingListId());
            
            repository.deleteAsync(packingList).join();
        });
    }
}
