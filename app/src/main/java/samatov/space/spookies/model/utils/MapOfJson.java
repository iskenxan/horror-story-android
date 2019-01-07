package samatov.space.spookies.model.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MapOfJson<String, T> implements ParameterizedType
{
    private Class<?> wrapped;

    public MapOfJson(Class<T> wrapper)
    {
        this.wrapped = wrapper;
    }

    @Override
    public Type[] getActualTypeArguments()
    {
        return new Type[] { wrapped };
    }

    @Override
    public Type getRawType()
    {
        return Map.class;
    }

    @Override
    public Type getOwnerType()
    {
        return null;
    }
}