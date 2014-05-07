package cisc_sim;




public class Cache {
	public boolean write_back;
	private int data;
	private static final int cache_size = 1024;
	private int[] cache;
	Cache(){
		this.cache=new int[cache_size];
		for(int i=0;i<cache_size;i++){
			cache[i]=0;
		}
	}
	public void write(int addr,int data){
		int location=addr%cache_size;
		int tag_addr=(addr&0x7C00)<<6;
		int entry=data+tag_addr;
		cache[location]=data;
		this.SetValid(addr);
	}
	public boolean ReadCache(int addr){
		if(IsValid(addr))
		{
			int location=addr%cache_size;
			int tag_addr=(addr&0x7C00)<<6;
			int tag_location=cache[location]&0x1F0000;
			if(tag_location==tag_addr){
				this.data=read(location);
				return true;
			}
		}
		return false;
	}
	private void SetValid(int addr){
		int location=addr%cache_size;
		cache[location]=cache[location]|0x200000;
	}
	public void SetInvalid(int addr){
		int location=addr%cache_size;
		cache[location]=cache[location]&0xFFDFFFFF;
	}
	public void SetDirty(int addr){
		this.SetInvalid(addr);
	}
	public boolean IsValid(int addr){
		int location=addr%cache_size;
		if((cache[location]&0x200000)==0)
		{
			return false;	
		}
		else
			return true;
	}
	public int GetData(){
		return data;
	}
	public int read(int location){
		return cache[location]&0xFFFF;
	}
}

