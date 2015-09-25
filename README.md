# Custom Server Items

*Custom Server Items* allows to create custom items on a running server.
This can be used to create items for other mods to use like currency or decorative items.

This mod was mainly created to use it for ForgeEssentials permission items to create items like keys and other items that grant permissions when equipped.

 
## Usage

Simply place some icon files (e.g. "key.png") in "config/CustomServerItems" on the server.

After that you can get a `Custom Item` from the Miscellaneous creative tab or with `/give` and use the command
`/customServerItem texture key` to set the texture of the item.
You can also set name, tooltip, damage and other settings of the item with `/customServerItem`.

````
/customServerItem texturelist
/customServerItem texture <id>
/customServerItem name <value>
/customServerItem tooltip <value>
/customServerItem damage <value>
/customServerItem durability <value>
````
