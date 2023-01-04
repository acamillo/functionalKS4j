package fks4j.utillity;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class is combines the ability of creating, releasing and using a resource of type A
 * @param <A>
 */
final class Resource<A> {

    /**
     * Use ths method to create a Resource instance. The caller must provide the methods for acquiring and releasing the desired resource.
     * @param acquire the method for acquiring the resource
     * @param release th method for releasing the resource
     * @return a Resource
     * @param <A> the resource type.
     */
    public static <A> Resource<A> make(Supplier<A> acquire, Consumer<A> release) {
        return new Resource<>(acquire, release);
    }

//    public static <A extends AutoCloseable> Resource<A> fromAutoCloseable(Supplier<A> acquire) {
//        Consumer<A> release = a -> {
//            try {
//                acquire.get().close();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        };
//        return new Resource<>(acquire, release);
//    }

    /**
     * This method will try to acquire the resource, pass it to the resource consumer, and unconditionally release it
     * after the consumer either terminates or fail
     * @param f the resource consumer
     * @return the output of the consumer
     * @param <T> the consumer output data type
     */
    <T> T use(Function<A, T> f) {
        A resource = null;

        try {
            resource = acquire.get();
            return f.apply(resource);
        } finally {
            // The release might still fail and throw exception.
            // In this case better close and investigate the why.
            if (resource != null) release.accept(resource);
        }
    }
    private final Supplier<A> acquire;
    private final Consumer<A> release;


    private Resource(Supplier<A> acquire, Consumer<A> release) {
        this.acquire = acquire;
        this.release = release;
    }


}