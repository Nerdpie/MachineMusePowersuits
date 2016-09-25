package net.machinemuse.utils;

import net.machinemuse.powersuits.entity.TileEntityPortal;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.*;

public class MPSTeleporter extends Teleporter {
    private final WorldServer worldServerInstance;
    private final Random random;
    private final HashMap destinationCoordinateCache = new HashMap();
    private final List destinationCoordinateKeys = new ArrayList();

    public MPSTeleporter(WorldServer par1WorldServer) {
        super(par1WorldServer);
        this.worldServerInstance = par1WorldServer;
        this.random = new Random(par1WorldServer.getSeed());
    }

    public void placeInPortal(Entity entity, double x, double y, double z, float r) {
        if (!placeInExistingPortal(entity, x, y, z, r)) {
            if (this.worldServerInstance.provider.getDimension() != -1) {
                y = this.worldServerInstance.getTopSolidOrLiquidBlock(new BlockPos((int) x, 0, (int) z)).getY();
                entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0F);
            } else {
                makePortal(entity);
            }
        }
    }

    public TileEntity findPortalInChunk(double x, double z) {
        Chunk chunk = this.worldServerInstance.getChunkFromBlockCoords(new BlockPos((int) x, 0, (int) z));
        Iterator t = chunk.getTileEntityMap().values().iterator();
        while (t.hasNext()) {
            Object tile = t.next();
            if ((tile instanceof TileEntityPortal)) {
                return (TileEntity) tile;
            }
        }
        return null;
    }

    public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float r) {
        TileEntity destPortal = null;
        for (int s = 0; (s <= 5) && (destPortal == null); s++) {
            for (int dx = -s; dx <= s; dx++) {
                for (int dz = -s; dz <= s; dz++) {
                    if (destPortal == null) {
                        destPortal = findPortalInChunk(x + dx * 16, z + dz * 16);
                    }
                }
            }
        }
        if (destPortal != null) {
            entity.setLocationAndAngles(destPortal.getPos().getX() + 0.5D, destPortal.getPos().getY() + 1,
                    destPortal.getPos().getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
            entity.motionX = (entity.motionY = entity.motionZ = 0.0D);
            return true;
        }
        return false;
    }

    public boolean makePortal(Entity entity) {
        int ex = MathHelper.floor_double(entity.posX);
        int ey = MathHelper.floor_double(entity.posY) - 1;
        int ez = MathHelper.floor_double(entity.posZ);


        ey /= 5;
        ey += 22;
        if (ey > 247) {
            ey = 247;
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                for (int y = -2; y <= 4; y++) {
                    if ((x == 0) && (y == -1) && (z == 0)) {
                        // this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, Block.blockDiamond.blockID, 1, 2);
                        // this.worldServerInstance.scheduleBlockUpdate(ex + x, ey + y, ez + z, Block.blockDiamond.blockID, 1);
                    } else if ((y <= -2)) {
                        this.worldServerInstance.setBlockState(new BlockPos(ex + x, ey + y, ez + z), Blocks.STONE.getDefaultState());
                    } else if ((y == 0) && ((x == 2) || (x == -2) || (z == 2) || (z == -2))) {
                        //this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, 0, 5, 3);
                    } else {
                        this.worldServerInstance.setBlockState(new BlockPos(ex + x, ey + y, ez + z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        entity.setLocationAndAngles(ex + 0.5D, ey, ez + 0.5D, entity.rotationYaw, 0.0F);
        entity.motionX = (entity.motionY = entity.motionZ = 0.0D);

        return true;
    }

    public void removeStalePortalLocations(long par1) {
        if (par1 % 100L == 0L) {
            Iterator iterator = this.destinationCoordinateKeys.iterator();
            long j = par1 - 600L;
            while (iterator.hasNext()) {
                Long olong = (Long) iterator.next();
                PortalPosition portalposition = (PortalPosition) this.destinationCoordinateCache.get(olong.longValue());
                if ((portalposition == null) || (portalposition.lastUpdateTime < j)) {
                    iterator.remove();
                    this.destinationCoordinateCache.remove(olong.longValue());
                }
            }
        }
    }
}
