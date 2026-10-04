package stream;

import fpinjava.*;
import list.List;
import tuple.Tuple;

import static fpinjava.TailCall.ret;
import static fpinjava.TailCall.sus;

//sup class
public abstract class Stream<A> {

    private static Stream EMPTY = new Empty();


    public abstract A head();
    public abstract Stream<A> tail();
    public abstract Boolean isEmpty();
    private Stream() {}

    public abstract Result<A> headOption();
    public abstract List<A> toList();
    public abstract Stream<A> take(int n);
    public abstract Stream<A> drop(int n);
    public abstract Stream<A> takeWhile(Function<A, Boolean> p);
    public abstract Stream<A> dropWhile(Function<A, Boolean> p);
    public abstract <B> Stream<B> map(Function<A, B> f);
    public abstract void forEach(Effect<A> ef);

    @SuppressWarnings("unchecked")
    public static <A> Stream<A> empty() {
        return EMPTY;
    }

    public static <A> Stream<A> cons(Supplier<A> hd, Supplier<Stream<A>> tl) {
        return new Cons<>(hd, tl);
    }

    public static <A, S> Stream<A> unfold(S z, Function<S, Result<Tuple<A, S>>> f) {

        return f.apply(z)
                .map(x -> cons(() -> x.fst, () -> unfold(x.snd, f)))
                .getOrElse(empty());
    }

    public static Stream<Integer> from(int i) {
        return unfold(i, x -> Result.success(Tuple.tuple(x, x + 1)));
    }

    //class Empty
    private static class Empty<A> extends Stream<A> {

        @Override
        public Stream<A> tail() {
            throw new IllegalStateException("tail called on empty");
        }

        @Override
        public A head() {
            throw new IllegalStateException("head called on empty");
        }

        @Override
        public Boolean isEmpty() {
            return true;
        }

        @Override
        public Result<A> headOption() {
            return Result.empty();
        }

        @Override
        public List<A> toList() {
            return List.list();
        }

        @Override
        public Stream<A> take(int n) {
            return this;
        }

        @Override
        public Stream<A> drop(int n) {
            return this;
        }

        @Override
        public Stream<A> takeWhile(Function<A, Boolean> p) {
            return this;
        }

        @Override
        public Stream<A> dropWhile(Function<A, Boolean> p) {
            return this;
        }

        @Override
        public <B> Stream<B> map(Function<A, B> f) {
            return empty();
        }

        @Override
        public void forEach(Effect<A> ef) {

        }
    }

    //class Cons
    private static class Cons<A> extends Stream<A> {

        private final Supplier<A> head;
        private A h;
        private final Supplier<Stream<A>> tail;
        private Stream<A> t;

        private Cons(Supplier<A> h, Supplier<Stream<A>> t) {
            this.head = h;
            this.tail = t;
        }

        //if something breaks maybe here
        @Override
        public A head() {
            return h == null ? h = head.get() : h;
        }

        //also here
        @Override
        public Stream<A> tail() {
            return t == null ? t = tail.get() : t;
        }

        @Override
        public Boolean isEmpty() {
            return false;
        }

        @Override
        public Result<A> headOption() {
            return Result.success(head());
        }

        @Override
        public List<A> toList() {
            return toList(this, List.list()).eval();
        }

        private TailCall<List<A>> toList(Stream<A>s, List<A> acc) {

            return s.isEmpty()
                    ? ret(acc)
                    : sus(() -> toList(s.tail(), acc.cons(s.head())));
        }

        @Override
        public Stream<A> take(int n) {
            return n <= 0
                    ? empty()
                    : cons(head, () -> tail().take(n - 1));
        }

        @Override
        public Stream<A> drop(int n) {
            return drop(this, n).eval();
        }

        private TailCall<Stream<A>> drop(Stream<A> acc, int n) {
            return n <= 0
                    ? ret(acc)
                    : sus(() -> drop(acc.tail(), n - 1));
        }

        @Override
        public Stream<A> takeWhile(Function<A, Boolean> p) {

            return p.apply(head())
                    ? cons(head, () -> tail().takeWhile(p))
                    : empty();
        }

        @Override
        public Stream<A> dropWhile(Function<A, Boolean> p) {

            return dropWhile(this, p).eval();
        }

        private TailCall<Stream<A>> dropWhile(Stream<A> acc, Function<A, Boolean> p) {

            return acc.isEmpty()
                    ? ret(acc)
                    : p.apply(acc.head())
                        ? sus(() -> dropWhile(acc.tail(), p))
                        : ret(acc);
        }

        public Stream<A> repeat(A a) {

            return iterate(() -> a, x -> x);
        }

        public static <A> Stream<A> iterate(Supplier<A> seed, Function<A, A> f) {

            return cons(seed, () -> iterate(() -> f.apply(seed.get()), f));
        }

        @Override
        public <B> Stream<B> map(Function<A, B> f) {

            return cons(() -> f.apply(head.get()),
                        () -> tail.get().map(f));
        }

        public static Stream<Integer> fibs() {

            return unfold(Tuple.tuple(1, 1),
                    x -> Result.success(Tuple.tuple(x.fst, Tuple.tuple(x.snd, x.fst + x.snd))));
        }

        @Override
        public void forEach(Effect<A> ef) {

            ef.apply(head.get());
            tail.get().forEach(ef);
        }
    }
}
