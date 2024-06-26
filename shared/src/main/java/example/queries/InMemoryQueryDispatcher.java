package example.queries;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class InMemoryQueryDispatcher implements QueryDispatcher, ApplicationContextAware {

    public ApplicationContext applicationContext;


    @Override
    public <TResult> CompletableFuture<TResult> query(Query<TResult> query) {

        String queryName = query.getClass().getSimpleName();
        queryName = queryName.substring(0, 1).toLowerCase() + queryName.substring(1);
        var type = query.getClass().getGenericSuperclass();

        Object handlerBean = applicationContext.getBean(queryName + "Handler");
        if (handlerBean instanceof QueryHandler) {
            QueryHandler<Query<TResult>, TResult> handler = (QueryHandler<Query<TResult>, TResult>) handlerBean;
            return handler.handleAsync(query);
        } 
        throw new IllegalArgumentException("Wrong handler");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
