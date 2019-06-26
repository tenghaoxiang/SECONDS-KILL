local lockKey = KEYS[1]
local lockValue = KEYS[2]

local value = redis.call('get', lockKey)
if value == lockValue
then
local result = redis.call('del', lockKey)
return result
else
return false
end