# day16ws
http://localhost:3000/api/boardgame
POST
raw JSON
{
    "name": "Chess",
    "year": 1475,
    "players": 2,
    "genre": "Classic",
    "description": "A timeless strategy game."
}

keys * - display all keys
keys boardgame:* - find all key w pattern like that
get <redis key> - get the value

http://localhost:3000/api/boardgame/123e4567-e89b-12d3-a456-426614174000?upsert=true - if upsert true can put, if not true cannot
http://localhost:3000/api/boardgame/a84a6fc4-ea3b-415d-a6e3-b9b180732f42 - if exist can put, if not exist error

