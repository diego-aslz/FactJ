# FactJ

This API was built to be used in Java applications in order to build objects.
It helps, mainly, developers who create unit tests and need some organized
way of building objects to use while testing. It is similar to the
[FactoryGirl][1] project that is made for Ruby applications.

## Installation

Download the JAR from [here][2] and add it to your classpath.

## Factories

In order to build objects, you will need `factories`. You can create as
many of them as you need and from wherever you want in your code. Although, it's
a good idea to do it in some specific place. I like to put factories under
a `factories` package.

As an example, here is a factory to build objects of the Address class:

```java
package myapp.test.factories;

import static factj.FactJHelper.fabricate;
import static factj.FactJHelper.field;
import myapp.models.Address;

public abstract class AddressFactory {
  static {
    fabricate(Address.class,
        field("address", "Some address"));
  }

  /**
   * This method will be used to force the ClassLoader to load this
   * class and execute the static block above. I'll explain it better
   * later.
   */
  public static void load() { }
}
```

The method `fabricate` will create and register a new factory
of Address.class. This factory will create objects of type Address, setting
the field `address` with the value "Some address". There are
a few other ways to define the value a field must receive, but let's stay
simple for now in order to understand what's happening.

To use the factory, do:

```java
  // ...
    AddressFactory.load();
    Address address1 = (Address) FactJ.build(Address.class));
    System.out.println(address1.getAddress()); // Will print "Some address".
  // ...
```

In the first line, we called the method `load()` that does nothing.
Unfortunately this is necessary to force the ClassLoader to execute the
static block in the AddressFactory to create and register our factory in
FactJ. If you do not do this, the `FactJ.build()` will return
`null`, since it will not have the factory created. If you can think of
a better way of doing this, please let me know.

## Persisting Objects

If you are using FactJ to build objects to test your app, probably you'll want
to save the generated objects in a test database. To do so, you must set a
`Persistence` implementation in the FactJ class to manage it.

```java
package myapp.test;

import factj.Persistence;

public class MyPersistence implements Persistence {
  @Override
  public void save(Object o) {
    // Do your job and save the object.
    new GenericDao().persist(o);
  }
}
```

And, before you create objects:

```java
    FactJ.setPersistence(new MyPersistence());
    FactJ.create(Address.class);
```

Note that instead of `FactJ.build` we call `FactJ.create`
that does the same thing as the first one, but **saves** the
object after it's built. The method `MyPersistence.save` will be
called every time the `FactJ.create` method is invoked.

## Factory Overloading

If you, for some reason, need to create more than one factory for the same
Class, you can do this by naming them:

```java
package myapp.test.factories;

import static factj.FactJHelper.fabricate;
import static factj.FactJHelper.field;
import myapp.models.Person;

public abstract class PersonFactory {
  static {
    fabricate(Person.class,
        field("name", "Diego"),
        field("client", false));

    fabricate(Person.class, "clients",
        field("name", "Diego"),
        field("client", true));
  }

  // ...
}
```

Here we created two factories for Person.class. The first one doesn't have
a name and will be de default factory. However, the second one
**does** have a name, it's "clients".
Notice that the second one sets the field `client` to `true`. To use them:

```java
    PersonFactory.load();
    FactJ.build(Person.class); // will use the first one
    FactJ.build(Person.class, "clients"); // will use the second one
```

Using the name of the factory, FactJ will select the correct one to call.

## Customizing Fields

A Factory receives a bunch of `Decorator` objects in its constructor.
They are used to modify something in the object when it's built. For each object
built, all of the decorators will be invoked to customize it.

There are a few built-in helper methods that helps out defining decorators.
You already saw one of them: `field("address", "Some address")`.

### FieldDecorator

This decorator just assigns the given value to the given field, like you
saw on the first example.

```java
package myapp.test.factories;

import static factj.FactJHelper.fabricate;
import static factj.FactJHelper.field;
import myapp.models.Address;

public abstract class AddressFactory {
  static {
    fabricate(Address.class,
        field("address", "Some address"));
  }

  // ...
}
```

### SequenceDecorator

This decorator gives the field a unique value based on a sequence, similarly
to a database sequence:

```java
package myapp.test.factories;

import static factj.FactJHelper.fabricate;
import static factj.FactJHelper.field;
import static factj.FactJHelper.sequence;
import myapp.models.Person;

public abstract class PersonFactory {
  static {
    fabricate(Person.class,
        sequence("id"), // You may not want to set this field when persisting object to a database.
        field("name", "Diego"),
        field("email", "mymail@gmail.com"));
  }

  // ...
}
```

Each time you call `FactJ.build` or `FactJ.create`, the
new object's id field will receive, first, 1, then 2, then 3 and so on.

Now, let's suppose you validates the uniqueness of e-mails. We don't want to
create an invalid Person when creating the second one with the same e-mail as
the first one. Then:

```java
package myapp.test.factories;

import static factj.FactJHelper.fabricate;
import static factj.FactJHelper.field;
import static factj.FactJHelper.sequence;
import factj.decorators.SequenceDecorator.Sequence;
import myapp.models.Person;

public abstract class PersonFactory {
  static {
    fabricate(Person.class,
        sequence("id"), // You may not want to set this field when persisting object to a database.
        field("name", "Diego"),
        sequence("email", new Sequence() {
          @Override
          public Object generate(int count) {
            return "person" + count + "@gmail.com";
          }
        });
  }

  // ...
}
```

This way, the first person built will have the email person1@gmail.com, and
the second one will have person2@gmail.com and so on.

### AssociationDecorator

Very often, we have attributes that references other classes of ours. If you
need to populate attributes like this with objects built by FactJ,
AssociationDecorator is the way to go.

```java
package myapp.test.factories;

import static factj.FactJHelper.fabricate;
import static factj.FactJHelper.field;
import static factj.FactJHelper.association;
import myapp.models.Person;

public abstract class PersonFactory {
  static {
    fabricate(Person.class,
        field("name", "Diego"),
        association("address"));
  }

  // ...
}
```

That's it. The decorator that `association` returns will lookup for
the Class of the given field and invoke `FactJ.create` passing
the field's Class to it. The object returned by FactJ will be set on the field.

It is **important** to notice that every association will be
persisted in the database if you set the Persistence object.

Now, let's say you want to populate an association with an object of another
Class, different from that your attribute is declared with. You need to do this:

```java
    fabricate(Person.class,
        field("name", "Diego"),
        association("address", AnotherClass.class));
```

The FactJ will lookup for a factory for AnotherClass in order to build the
object. Be aware that you *need* to have a registered factory for AnotherClass.
Otherwise, the field will get null.

Also, it is possible to customize the name of the factory that you want to use:

```java
    fabricate(Person.class,
        field("name", "Diego"),
        association("address", "nameOfTheFactory"));
```

In this case, you *need* to have a registered factory for Address with the
name set to "nameOfTheFactory". Alternatively, you can customize both the
Class and the factory name:

```java
    fabricate(Person.class,
        field("name", "Diego"),
        association("address", AnotherClass.class, "nameOfTheFactory"));
```

### Custom Decorator

If the built-in decorators doesn't fit your needs, you can create your own:

```java
    fabricate(Person.class,
        field("name", "Diego"),
        new Decorator<Person>() {
          @Override
          public void decorate(Person p) {
            // Customize your object
            p.setCreatedAt(MyUtils.getCurrentTime());
          }
        });
```

You may need to do something like that, but, specifically in this example,
it's better to create a new `FieldDecorator` and just override its
method `getValue()`, like this:

```java
    fabricate(Person.class,
        field("name", "Diego"),
        new FieldDecorator("createdAt") {
          @Override
          public Object getValue() {
            return MyUtils.getCurrentTime();
          }
        });
```

## Customizing Objects "On The Fly"

Since version 1.1, it's possible to customize an object as you build it.
If, for example, you want to customize an object before it gets saved
and you don't want to modify your factory because it's a special case,
you can do this:

```java
    Address a = (Address) FactJ.create(Address.class, new Decorator<Address>() {
      @Override
      public void decorate(Address a) {
        a.setAddress("another");
      }
    });
```

This way, only the object built in this call will be customized by that
decorator. You can pass as many decorators as you want to the `create`
and `build` methods.

## Testing

There is a source folder named `test`. Just run the app as JUnit Test.

[1]:https://github.com/thoughtbot/factory_girl
[2]:https://sourceforge.net/projects/factj/files/
