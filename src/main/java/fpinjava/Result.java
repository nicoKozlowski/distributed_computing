package fpinjava;


import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Callable;

public abstract class Result<A> implements Serializable {

  private Result() {
  }

  public abstract Boolean isSuccess();
  public abstract Boolean isFailure();
  public abstract Boolean isEmpty();
  public abstract A getOrElse(final A defaultValue);
  public abstract A getOrElse(final Supplier<A> defaultValue);
  public abstract <B> B foldl(Function<B, Function<A, B>> f, final B identity);
  public abstract <B> B foldr(Function<A, Function<B, B>> f, final B identity);
  public abstract A successValue();
  public abstract Exception failureValue();
  public abstract void forEach(Effect<A> c);
  public abstract void forEachOrThrow(Effect<A> c);
  public abstract Result<String> forEachOrFail(Effect<A> e);
  public abstract Result<RuntimeException> forEachOrException(Effect<A> e);
  public abstract Result<A> filter(Function<A, Boolean> f);
  public abstract Result<A> filter(Function<A, Boolean> p, String message);
  public abstract <B> Result<B> map(Function<A, B> f);
  public abstract Result<A> mapFailure(String s, Exception e);
  public abstract Result<A> mapFailure(String s);
  public abstract Result<A> mapFailure(Exception e);
  public abstract Result<A> mapFailure(Result<A> v);
  public abstract Result<Nothing> mapEmpty();
  public abstract <B> Result<B> flatMap(Function<A, Result<B>> f);
  public abstract Boolean exists(Function<A, Boolean> f);


  // ap(Nothing, _ )          = Nothing
  // ap((Just f), this ) = map f this
  public <B> Result<B> ap(Result<Function<A, B>> rf) {
    if (rf.isSuccess()) {
      return map(rf.successValue());
    } else return empty();
  }

  public <B> Result<B> pure(B b){
    return success(b);
  }

  public <B> Result<B> bind(Function<A, Result<B>> f) {
    return flatMap(f);
  }

  public Result<A> orElse(Supplier<Result<A>> defaultValue) {
    return map(x -> this).getOrElse(defaultValue);
  }

  public static <T, U> Result<T> failure(Failure<U> failure) {
    return new Failure<>(failure.exception);
  }

  public static <A> Result<A> failure(String message) {
    return new Failure<>(message);
  }

  public static <A> Result<A> failure(String message, Exception e) {
    return new Failure<>(new IllegalStateException(message, e));
  }

  public static <A> Result<A> failure(Exception e) {
    return new Failure<>(e);
  }

  public static <A> Result<A> success(A value) {
    return new Success<>(value);
  }

  public static <A> Result<A> empty() {
    return new Empty<>();
  }

  public static <A> Result<A> flatten(Result<Result<A>> result) {
    return result.flatMap(x -> x);
  }


  private static class Failure<A> extends Empty<A> {

    private final RuntimeException exception;

    private Failure(String message) {
      super();
      this.exception = new IllegalStateException(message);
    }

    private Failure(RuntimeException e) {
      super();
      this.exception = e;
    }

    private Failure(Exception e) {
      super();
      this.exception = new IllegalStateException(e);
    }

    @Override
    public Boolean isSuccess() {
      return false;
    }

    @Override
    public Boolean isFailure() {
      return true;
    }

    @Override
    public A getOrElse(final A defaultValue) {
      return defaultValue;
    }

    @Override
    public A successValue() {
      throw new IllegalStateException("Method successValue() called on a Failure instance");
    }

    @Override
    public RuntimeException failureValue() {
      return this.exception;
    }

    @Override
    public void forEachOrThrow(Effect<A> c) {
      throw exception;
    }

    @Override
    public Result<RuntimeException> forEachOrException(Effect<A> c) {
      return success(exception);
    }

    @Override
    public Result<String> forEachOrFail(Effect<A> c) {
      return success(exception.getMessage());
    }

    @Override
    public Result<A> filter(Function<A, Boolean> f) {
      return failure(this);
    }

    @Override
    public Result<A> filter(Function<A, Boolean> p, String message) {
      return failure(this);
    }

    @Override
    public <U> Result<U> map(Function<A, U> f) {
      return failure(this);
    }

    @Override
    public Result<A> mapFailure(String s, Exception e) {
      return failure(s, e);
    }

    @Override
    public Result<A> mapFailure(String s) {
      return failure(s, exception);
    }

    @Override
    public Result<A> mapFailure(Exception e) {
      return failure(e.getMessage(), e);
    }

    @Override
    public Result<A> mapFailure(Result<A> v) {
      return v;
    }

    @Override
    public Result<Nothing> mapEmpty() {
      return failure(this);
    }

    @Override
    public <U> Result<U> flatMap(Function<A, Result<U>> f) {
      return failure(exception.getMessage(), exception);
    }


    @Override
    public String toString() {
      return String.format("Failure(%s)", failureValue());
    }

    @Override
    public Boolean exists(Function<A, Boolean> f) {
      return false;
    }

    @Override
    public A getOrElse(Supplier<A> defaultValue) {
      return defaultValue.get();
    }

    @Override
    public boolean equals(Object o){
    	return (this == o || o instanceof Failure);
    }
    
    @Override
    public int hashCode(){
    	return 0;
    }

  }

  private static class Empty<A> extends Result<A> {

    public Empty() {
      super();
    }

    @Override
    public Boolean isSuccess() {
      return false;
    }

    @Override
    public Boolean isFailure() {
      return false;
    }

    @Override
    public Boolean isEmpty() {
      return true;
    }

    @Override
    public A getOrElse(final A defaultValue) {
      return defaultValue;
    }

    @Override
    public A successValue() {
      throw new IllegalStateException("Method successValue() called on a Empty instance");
    }

    @Override
    public RuntimeException failureValue() {
      throw new IllegalStateException("Method failureMessage() called on a Empty instance");
    }

    @Override
    public void forEach(Effect<A> c) {
      /* Empty. Do nothing. */
    }

    @Override
    public void forEachOrThrow(Effect<A> c) {
      /* Do nothing */
    }

    @Override
    public Result<String> forEachOrFail(Effect<A> c) {
      return empty();
    }

    @Override
    public Result<RuntimeException> forEachOrException(Effect<A> c) {
      return empty();
    }

    @Override
    public Result<A> filter(Function<A, Boolean> f) {
      return empty();
    }

    @Override
    public Result<A> filter(Function<A, Boolean> p, String message) {
      return empty();
    }

    @Override
    public <U> Result<U> map(Function<A, U> f) {
      return empty();
    }

    @Override
    public Result<A> mapFailure(String s, Exception e) {
      return failure(s, e);
    }

    @Override
    public Result<A> mapFailure(String s) {
      return failure(s);
    }

    @Override
    public Result<A> mapFailure(Exception e) {
      return failure(e.getMessage(), e);
    }

    @Override
    public Result<A> mapFailure(Result<A> v) {
      return v;
    }

    @Override
    public Result<Nothing> mapEmpty() {
      return success(Nothing.instance);
    }

    @Override
    public <B> Result<B> flatMap(Function<A, Result<B>> f) {
      return empty();
    }

    @Override
    public String toString() {
      return "Empty()";
    }

    @Override
    public Boolean exists(Function<A, Boolean> f) {
      return false;
    }

    @Override
    public A getOrElse(Supplier<A> defaultValue) {
      return defaultValue.get();
    }

    @Override
    public <B> B foldl(Function<B, Function<A, B>> f, B identity) {
      return identity;
    }

    @Override
    public <B> B foldr(Function<A, Function<B, B>> f, B identity) {
      return identity;
    }

    @Override
    public boolean equals(Object o){
    	return (this == o || o instanceof Empty);
    }
    
    @Override
    public int hashCode(){
    	return 0;
    }

  }

  private static class Success<A> extends Result<A> {

    private final A value;

    public Success(A value) {
      super();
      this.value = value;
    }

    @Override
    public Boolean isSuccess() {
      return true;
    }

    @Override
    public Boolean isFailure() {
      return false;
    }

    @Override
    public Boolean isEmpty() {
      return false;
    }

    @Override
    public A getOrElse(final A defaultValue) {
      return successValue();
    }

    @Override
    public A successValue() {
      return this.value;
    }

    @Override
    public RuntimeException failureValue() {
      throw new IllegalStateException("Method failureValue() called on a Success instance");
    }

    @Override
    public void forEach(Effect<A> e) {
      e.apply(this.value);
    }

    @Override
    public void forEachOrThrow(Effect<A> e) {
      e.apply(this.value);
    }

    @Override
    public Result<String> forEachOrFail(Effect<A> e) {
      e.apply(this.value);
      return empty();
    }

    @Override
    public Result<RuntimeException> forEachOrException(Effect<A> e) {
      e.apply(this.value);
      return empty();
    }

    @Override
    public Result<A> filter(Function<A, Boolean> p) {
      return filter(p, "Unmatched predicate with no error message provided.");
    }

    @Override
    public Result<A> filter(Function<A, Boolean> p, String message) {
      try {
        return p.apply(successValue())
            ? this
            : failure(message);
      } catch (Exception e) {
        return failure(e.getMessage(), e);
      }
    }

    // fmap f (Just x) = Just (f x)
    @Override
    public <U> Result<U> map(Function<A, U> f) {
      try {
        return success(f.apply(successValue()));
      } catch (Exception e) {
        return failure(e.getMessage(), e);
      }
    }

    @Override
    public Result<A> mapFailure(String f, Exception e) {
      return this;
    }

    @Override
    public Result<A> mapFailure(String s) {
      return this;
    }

    @Override
    public Result<A> mapFailure(Exception e) {
      return this;
    }

    @Override
    public Result<A> mapFailure(Result<A> v) {
      return this;
    }

    @Override
    public Result<Nothing> mapEmpty() {
      return failure("Not empty");
    }

    @Override
    public <B> Result<B> flatMap(Function<A, Result<B>> f) {
      try {
        return f.apply(successValue());
      } catch (Exception e) {
        return failure(e.getMessage());
      }
    }

    @Override
    public String toString() {
      return String.format("Success(%s)", successValue().toString());
    }

    @Override
    public Boolean exists(Function<A, Boolean> f) {
      return f.apply(successValue());
    }

    @Override
    public A getOrElse(Supplier<A> defaultValue) {
      return successValue();
    }

    @Override
    public <B> B foldl(Function<B, Function<A, B>> f, B identity) {
      return f.apply(identity).apply(successValue());
    }

    @Override
    public <B> B foldr(Function<A, Function<B, B>> f, B identity) {
      return f.apply(successValue()).apply(identity);
    }

    @Override
    public boolean equals(Object o){
    	return (this == o|| o instanceof Success)
    			&& this.value.equals(((Success<?>) o).value);
    }
    
    @Override
    public int hashCode() {
    	return Objects.hashCode(value);
    }

  }

  public static <A> Result<A> of(final Callable<A> callable) {
    return of(callable, "Null value");
  }

  public static <A> Result<A> of(final Callable<A> callable,
                                 final String message) {
    try {
      A value = callable.call();
      return value == null
          ? Result.failure(message)
          : Result.success(value);
    } catch (Exception e) {
      return Result.failure(e.getMessage(), e);
    }
  }

  public static <A> Result<A> of(final Function<A, Boolean> predicate,
                                 final A value,
                                 final String message) {
    try {
      return predicate.apply(value)
          ? Result.success(value)
          : Result.failure(String.format(message, value));
    } catch (Exception e) {
      String errMessage = String.format("Exception while evaluating predicate: %s", String.format(message, value));
      return Result.failure(errMessage, e);
    }
  }

  public static <A> Result<A> of(final A value) {
    return value != null
        ? success(value)
        : Result.<A>failure("Null value");
  }

  public static <A> Result<A> of(final A value, final String message) {
    return value != null
        ? Result.success(value)
        : Result.failure(message);
  }

  public static <A, B> Function<Result<A>, Result<B>> lift(final Function<A, B> f) {
    return x -> x.map(f);
  }

  public static <A, B, C> Function<Result<A>, Function<Result<B>, Result<C>>> lift2(final Function<A, Function<B, C>> f) {
    return a -> b -> a.map(f).flatMap(b::map);
  }

  public static <A, B, C, D> Function<Result<A>, Function<Result<B>, Function<Result<C>, Result<D>>>> lift3(final Function<A, Function<B, Function<C, D>>> f) {
    return a -> b -> c -> a.map(f).flatMap(b::map).flatMap(c::map);
  }

  @SuppressWarnings("unchecked")
  public static <A, B, C> Result<C> map2_(final Result<A> a,
                                         final Result<B> b,
                                         final Function<A, Function<B, C>> f) {
    return a.isSuccess()
        ? b.isSuccess()
            ? Result.of(() -> f.apply(a.successValue()).apply(b.successValue()))
            : Result.failure((Failure<C>) b)
        : b.isSuccess()
            ? Result.failure((Failure<C>) a)
            : Result.failure(String.format("%s, %s", a.failureValue(), b.failureValue()));
  }

  public static <A, B, C> Result<C> map2(final Result<A> a,
                                         final Result<B> b,
                                         final Function<A, Function<B, C>> f) {
    return lift2(f).apply(a).apply(b);
  }

  public static <A> Result<A> join(Result<Result<A>> mm){
    return mm.flatMap(m->m);
  }

  public static <A,B> B maybe(B b, Function<A,B> f, Result<A> ra){
    return ra.map(f).getOrElse(b);
  }
}
