package cisc_sim;
public class FALU {
	public float add(int a,int b){
		float fa=this.convertToFloat(a);
		float fb=this.convertToFloat(b);
		return fb+fa;
		
	}
	public float sub(int a, int b){
		float fa=this.convertToFloat(a);
		float fb=this.convertToFloat(b);
		return fb+fa;
	}
	public float convertToFloat(int a){
		float result;
		float sign=a&0x8000;
		int exponent=((a&0x7f00)>>8)-63;
		float mantissa=(float)(a&0xff|0x100)/(float)Math.pow(2, 8);
		if(sign==0){
			result=mantissa*(float)Math.pow(2, exponent);
		}
		else{
			result=-mantissa*(float)Math.pow(2, exponent);
		}
		return result;
	}
	public int convertToInt(float a){
		int a_32=Float.floatToIntBits(a);
		int sign=(a_32&0x80000000)>>16;
		int exponent=0;
		int exponent_32=((a_32&0x7f800000)>>23)-127;
		
		if(exponent_32>42||exponent_32<-64){
			// The float cannot be represented in 16 bits
			 
		}
		else{
			exponent = (exponent_32+63)<<8;
		}
		int mantissa=(a_32&0x7fffff)>>15;
		return sign+exponent+mantissa;
	}

}
