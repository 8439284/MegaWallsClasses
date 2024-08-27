//package org.ajls.megawallsclasses;
//
//public class ChunkUtils {
//
//    private uint GetGlobalPaletteIDFromState(BlockState state) {
//        // Implementation left to the user; see Data Generators for more info on the values
//    }
//
//    private BlockState GetStateFromGlobalPaletteID(uint value) {
//        // Implementation left to the user; see Data Generators for more info on the values
//    }
//
//    public interface Palette {
//        uint IdForState(BlockState state);
//        BlockState StateForId(uint id);
//        byte GetBitsPerBlock();
//        void Read(Buffer data);
//        void Write(Buffer data);
//    }
//
//    public class IndirectPalette : Palette {
//        Map<uint, BlockState> idToState;
//        Map<BlockState, uint> stateToId;
//        byte bitsPerBlock;
//
//    public IndirectPalette(byte palBitsPerBlock) {
//            bitsPerBlock = palBitsPerBlock;
//        }
//
//        public uint IdForState(BlockState state) {
//            return stateToId.Get(state);
//        }
//
//        public BlockState StateForId(uint id) {
//            return idToState.Get(id);
//        }
//
//        public byte GetBitsPerBlock() {
//            return bitsPerBlock;
//        }
//
//        public void Read(Buffer data) {
//            idToState = new Map<>();
//            stateToId = new Map<>();
//            // Palette Length
//            int length = ReadVarInt();
//            // Palette
//            for (int id = 0; id < length; id++) {
//                uint stateId = ReadVarInt();
//                BlockState state = GetStateFromGlobalPaletteID(stateId);
//                idToState.Set(id, state);
//                stateToId.Set(state, id);
//            }
//        }
//
//        public void Write(Buffer data) {
//            Assert(idToState.Size() == stateToId.Size()); // both should be equivalent
//            // Palette Length
//            WriteVarInt(idToState.Size());
//            // Palette
//            for (int id = 0; id < idToState.Size(); id++) {
//                BlockState state = idToState.Get(id);
//                uint stateId = GetGlobalPaletteIDFromState(state);
//                WriteVarInt(stateId);
//            }
//        }
//    }
//
//    public class DirectPalette : Palette {
//        public uint IdForState(BlockState state) {
//            return GetGlobalPaletteIDFromState(state);
//        }
//
//        public BlockState StateForId(uint id) {
//            return GetStateFromGlobalPaletteID(id);
//        }
//
//        public byte GetBitsPerBlock() {
//            return Ceil(Log2(BlockState.TotalNumberOfStates)); // currently 15
//        }
//
//        public void Read(Buffer data) {
//            // No Data
//        }
//
//        public void Write(Buffer data) {
//            // No Data
//        }
//    }
//
//    public Palette ChoosePalette(byte bitsPerBlock) {
//        if (bitsPerBlock <= 4) {
//            return new IndirectPalette(4);
//        } else if (bitsPerBlock <= 8) {
//            return new IndirectPalette(bitsPerBlock);
//        } else {
//            return new DirectPalette();
//        }
//    }
//
//
//    public Chunk ReadChunkDataPacket(Buffer data) {
//        int x = ReadInt(data);
//        int z = ReadInt(data);
//        bool full = ReadBool(data);
//        Chunk chunk;
//        if (full) {
//            chunk = new Chunk(x, z);
//        } else {
//            chunk = GetExistingChunk(x, z);
//        }
//        int mask = ReadVarInt(data);
//        int size = ReadVarInt(data);
//        ReadChunkColumn(chunk, full, mask, data.ReadByteArray(size));
//
//        int blockEntityCount = ReadVarInt(data);
//        for (int i = 0; i < blockEntityCount; i++) {
//            CompoundTag tag = ReadCompoundTag(data);
//            chunk.AddBlockEntity(tag.GetInt("x"), tag.GetInt("y"), tag.GetInt("z"), tag);
//        }
//
//        return chunk;
//    }
//
//    private void ReadChunkColumn(Chunk chunk, bool full, int mask, Buffer data) {
//        for (int sectionY = 0; sectionY < (CHUNK_HEIGHT / SECTION_HEIGHT); y++) {
//            if ((mask & (1 << sectionY)) != 0) {  // Is the given bit set in the mask?
//                byte bitsPerBlock = ReadByte(data);
//                Palette palette = ChoosePalette(bitsPerBlock);
//                palette.Read(data);
//
//                // A bitmask that contains bitsPerBlock set bits
//                uint individualValueMask = (uint)((1 << bitsPerBlock) - 1);
//
//                int dataArrayLength = ReadVarInt(data);
//                UInt64[] dataArray = ReadUInt64Array(data, dataArrayLength);
//
//                ChunkSection section = new ChunkSection();
//
//                for (int y = 0; y < SECTION_HEIGHT; y++) {
//                    for (int z = 0; z < SECTION_WIDTH; z++) {
//                        for (int x = 0; x < SECTION_WIDTH; x++) {
//                            int blockNumber = (((y * SECTION_HEIGHT) + z) * SECTION_WIDTH) + x;
//                            int startLong = (blockNumber * bitsPerBlock) / 64;
//                            int startOffset = (blockNumber * bitsPerBlock) % 64;
//                            int endLong = ((blockNumber + 1) * bitsPerBlock - 1) / 64;
//
//                            uint data;
//                            if (startLong == endLong) {
//                                data = (uint)(dataArray[startLong] >> startOffset);
//                            } else {
//                                int endOffset = 64 - startOffset;
//                                data = (uint)(dataArray[startLong] >> startOffset | dataArray[endLong] << endOffset);
//                            }
//                            data &= individualValueMask;
//
//                            // data should always be valid for the palette
//                            // If you're reading a power of 2 minus one (15, 31, 63, 127, etc...) that's out of bounds,
//                            // you're probably reading light data instead
//
//                            BlockState state = palette.StateForId(data);
//                            section.SetState(x, y, z, state);
//                        }
//                    }
//                }
//
//                for (int y = 0; y < SECTION_HEIGHT; y++) {
//                    for (int z = 0; z < SECTION_WIDTH; z++) {
//                        for (int x = 0; x < SECTION_WIDTH; x += 2) {
//                            // Note: x += 2 above; we read 2 values along x each time
//                            byte value = ReadByte(data);
//
//                            section.SetBlockLight(x, y, z, value & 0xF);
//                            section.SetBlockLight(x + 1, y, z, (value >> 4) & 0xF);
//                        }
//                    }
//                }
//
//                if (currentDimension.HasSkylight()) { // IE, current dimension is overworld / 0
//                    for (int y = 0; y < SECTION_HEIGHT; y++) {
//                        for (int z = 0; z < SECTION_WIDTH; z++) {
//                            for (int x = 0; x < SECTION_WIDTH; x += 2) {
//                                // Note: x += 2 above; we read 2 values along x each time
//                                byte value = ReadByte(data);
//
//                                section.SetSkyLight(x, y, z, value & 0xF);
//                                section.SetSkyLight(x + 1, y, z, (value >> 4) & 0xF);
//                            }
//                        }
//                    }
//                }
//
//                // May replace an existing section or a null one
//                chunk.Sections[SectionY] = section;
//            }
//        }
//
//        for (int z = 0; z < SECTION_WIDTH; z++) {
//            for (int x = 0; x < SECTION_WIDTH; x++) {
//                chunk.SetBiome(x, z, ReadInt(data));
//            }
//        }
//    }
//
//
//    public void WriteChunkDataPacket(Chunk chunk, Buffer data) {
//        WriteInt(data, chunk.GetX());
//        WriteInt(data, chunk.GetZ());
//        WriteBool(true);  // Full
//
//        int mask = 0;
//        Buffer columnBuffer = new Buffer();
//        for (int sectionY = 0; sectionY < (CHUNK_HEIGHT / SECTION_HEIGHT); y++) {
//            if (!chunk.IsSectionEmpty(sectionY)) {
//                mask |= (1 << chunkY);  // Set that bit to true in the mask
//                WriteChunkSection(chunk.Sections[sectionY], columnBuffer);
//            }
//        }
//        for (int z = 0; z < SECTION_WIDTH; z++) {
//            for (int x = 0; x < SECTION_WIDTH; x++) {
//                WriteInt(columnBuffer, chunk.GetBiome(x, z));  // Use 127 for 'void' if your server doesn't support biomes
//            }
//        }
//
//        WriteVarInt(data, mask);
//        WriteVarInt(data, columnBuffer.Size);
//        WriteByteArray(data, columnBuffer);
//
//        // If you don't support block entities yet, use 0
//        // If you need to implement it by sending block entities later with the update block entity packet,
//        // do it that way and send 0 as well.  (Note that 1.10.1 (not 1.10 or 1.10.2) will not accept that)
//
//        WriteVarInt(data, chunk.BlockEntities.Length);
//        foreach (CompoundTag tag in chunk.BlockEntities) {
//            WriteCompoundTag(data, tag);
//        }
//    }
//
//    private void WriteChunkSection(ChunkSection section, Buffer buf) {
//        Palette palette = section.palette;
//        byte bitsPerBlock = palette.GetBitsPerBlock();
//
//        WriteByte(bitsPerBlock);
//        palette.Write(buf);
//
//        int dataLength = (16*16*16) * bitsPerBlock / 64; // See tips section for an explanation of this calculation
//        UInt64[] data = new UInt64[dataLength];
//
//        // A bitmask that contains bitsPerBlock set bits
//        uint individualValueMask = (uint)((1 << bitsPerBlock) - 1);
//
//        for (int y = 0; y < SECTION_HEIGHT; y++) {
//            for (int z = 0; z < SECTION_WIDTH; z++) {
//                for (int x = 0; x < SECTION_WIDTH; x++) {
//                    int blockNumber = (((y * SECTION_HEIGHT) + z) * SECTION_WIDTH) + x;
//                    int startLong = (blockNumber * bitsPerBlock) / 64;
//                    int startOffset = (blockNumber * bitsPerBlock) % 64;
//                    int endLong = ((blockNumber + 1) * bitsPerBlock - 1) / 64;
//
//                    BlockState state = section.GetState(x, y, z);
//
//                    UInt64 value = palette.IdForState(state);
//                    value &= individualValueMask;
//
//                    data[startLong] |= (value << startOffset);
//
//                    if (startLong != endLong) {
//                        data[endLong] = (value >> (64 - startOffset));
//                    }
//                }
//            }
//        }
//
//        WriteVarInt(dataLength);
//        WriteLongArray(data);
//
//        for (int y = 0; y < SECTION_HEIGHT; y++) {
//            for (int z = 0; z < SECTION_WIDTH; z++) {
//                for (int x = 0; x < SECTION_WIDTH; x += 2) {
//                    // Note: x += 2 above; we read 2 values along x each time
//                    byte value = section.GetBlockLight(x, y, z) | (section.GetBlockLight(x + 1, y, z) << 4);
//                    WriteByte(data, value);
//                }
//            }
//        }
//
//        if (currentDimension.HasSkylight()) { // IE, current dimension is overworld / 0
//            for (int y = 0; y < SECTION_HEIGHT; y++) {
//                for (int z = 0; z < SECTION_WIDTH; z++) {
//                    for (int x = 0; x < SECTION_WIDTH; x += 2) {
//                        // Note: x += 2 above; we read 2 values along x each time
//                        byte value = section.GetSkyLight(x, y, z) | (section.GetSkyLight(x + 1, y, z) << 4);
//                        WriteByte(data, value);
//                    }
//                }
//            }
//        }
//    }
//}
//
