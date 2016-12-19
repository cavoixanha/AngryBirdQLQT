package com.xdpm.angrybirds.utils;

import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.andengine.util.debug.Debug;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.res.AssetManager;

import com.xdpm.angrybirds.common.BarrierConstants;
import com.xdpm.angrybirds.gamelevel.GameLevel;
import com.xdpm.angrybirds.gamelevel.object.arbalest.Arbalest;
import com.xdpm.angrybirds.gamelevel.object.barrier.BaseBarrierObject;
import com.xdpm.angrybirds.gamelevel.object.barrier.IceBarrierObject;
import com.xdpm.angrybirds.gamelevel.object.barrier.StoneBarrierObject;
import com.xdpm.angrybirds.gamelevel.object.barrier.WoodBarrierObject;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.BlackBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.BlueBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.RedBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.WhiteBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.YellowBirdObject;
import com.xdpm.angrybirds.gamelevel.object.pig.BasePigObject;
import com.xdpm.angrybirds.gamelevel.object.pig.HelmetPigObject;
import com.xdpm.angrybirds.gamelevel.object.pig.KingPigObject;
import com.xdpm.angrybirds.gamelevel.object.pig.NormalPigObject;
import com.xdpm.angrybirds.gamelevel.object.pig.OldPigObject;
import com.xdpm.angrybirds.gamelevel.object.pig.SmallPigObject;
import com.xdpm.angrybirds.manager.ResourceManager;

public class XMLLevelLoader {
	
	// ===================================================================
	// XML fields
	// ===================================================================
	
	private static final String FILE_NAME_PATTERN = "level/level_%d_%d_%d.xml";
	
	private static final String TAG_ARBALEST = "arbalest";
	private static final String TAG_BIRD = "bird";
	private static final String TAG_PIG= "pig";
	private static final String TAG_BARRIER= "barrier";
	private static final String TAG_NEXT_LEVEL= "next-level";
	
	private static final String ATTRIBUTE_WORLD_ID= "worldId";
	private static final String ATTRIBUTE_STAGE_ID = "stageId";
	private static final String ATTRIBUTE_LEVEL_ID = "levelId";
	private static final String ATTRIBUTE_TYPE = "type";
	private static final String ATTRIBUTE_X_COORDINATE = "x";
	private static final String ATTRIBUTE_Y_COORDINATE = "y";
	private static final String ATTRIBUTE_CLASS = "class";
	private static final String ATTRIBUTE_ROTATION = "rotation";
	
	private static final String VALUE_BLACK_BIRD_TYPE = "black";
	private static final String VALUE_BLUE_BIRD_TYPE = "blue";
	private static final String VALUE_RED_BIRD_TYPE = "red";
	private static final String VALUE_WHITE_BIRD_TYPE = "white";
	private static final String VALUE_YELLOW_BIRD_TYPE = "yellow";
		
	private static final String VALUE_HELMET_PIG_TYPE = "helmet";
	private static final String VALUE_KING_PIG_TYPE = "king";
	private static final String VALUE_SMALL_PIG_TYPE = "small";
	private static final String VALUE_NORMAL_PIG_TYPE = "normal";
	private static final String VALUE_OLD_PIG_TYPE = "old";
	
	private static final String VALUE_ICE_BARRIER_TYPE = "ice";
	private static final String VALUE_STONE_BARRIER_TYPE = "stone";
	private static final String VALUE_WOOD_BARRIER_TYPE = "wood";
	
	private static final String VALUE_STRAIGHT_01_CLASS = "straight-01";
	private static final String VALUE_STRAIGHT_02_CLASS = "straight-02";
	private static final String VALUE_STRAIGHT_03_CLASS = "straight-03";
	private static final String VALUE_STRAIGHT_04_CLASS = "straight-04";
	
	private static final String VALUE_SQUARE_01_CLASS = "square-01";
	private static final String VALUE_SQUARE_02_CLASS = "square-02";
	private static final String VALUE_SQUARE_03_CLASS = "square-03";
	private static final String VALUE_SQUARE_HOLLOW_CLASS = "square-hollow";
	
	private static final String VALUE_TRIANGLE_FILLED_CLASS = "triangle-filled";
	private static final String VALUE_TRIANGLE_HOLLOW_CLASS = "triangle-hollow";
	
	private static final String VALUE_CIRCLE_BIG_CLASS = "circle-big";
	private static final String VALUE_CIRCLE_SMALL_CLASS = "circle-small";
	
	// ===================================================================
	// Properties
	// ===================================================================
	
	private String mX = "";
	private String mY = "";
	private String mType = "";
	private String mClass = "";
	private String mRotation = "";
	private String mNextWorldId = "";
	private String mNextStageId = "";
	private String mNextLevelId = "";
	
	private Arbalest mArbalest;
	private ArrayList<BaseBirdObject> mBirdList;
	private ArrayList<BasePigObject> mPigList;
	private ArrayList<BaseBarrierObject> mBarrierList;

	private GameLevel mCurrentGameLevel;
	
	// ===================================================================
	// Getters and Setters
	// ===================================================================
	
	public Arbalest getArbalest() {
		return this.mArbalest;
	}
	
	public ArrayList<BaseBirdObject> getBirdList() {
		return mBirdList;
	}
	
	public ArrayList<BasePigObject> getPigList() {
		return mPigList;
	}
	
	public ArrayList<BaseBarrierObject> getBarrierList() {
		return mBarrierList;
	}
	
	// ===================================================================
	// Constructors
	// ===================================================================
	
	public XMLLevelLoader(GameLevel pGameLevel) {
		this.mCurrentGameLevel = pGameLevel;
		this.mBirdList = new ArrayList<BaseBirdObject>();
		this.mPigList = new ArrayList<BasePigObject>();
		this.mBarrierList = new ArrayList<BaseBarrierObject>();
	}
	
	public void load() {
		int worldId = this.mCurrentGameLevel.getWorldId();
		int stageId = this.mCurrentGameLevel.getStageId();
		int levelId = this.mCurrentGameLevel.getLevelId();
		
		AssetManager assetManager = ResourceManager.getInstance().getBaseGameActivity().getAssets();
		
    	String fileName = String.format(FILE_NAME_PATTERN, worldId, stageId, levelId);
		Debug.e(fileName);
    	try {
    		
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            
            XmlHandler saxHandler = new XmlHandler();
            
            xmlReader.setContentHandler(saxHandler);
          
            xmlReader.parse(new InputSource(assetManager.open(fileName)));
 
        } catch (final Throwable pThrowable) {
            Debug.e("xmlLoader failed", pThrowable);
        }
	}
	
	public class XmlHandler extends DefaultHandler {
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			
			if (localName.equalsIgnoreCase(TAG_ARBALEST)) {
				
				mX = attributes.getValue(ATTRIBUTE_X_COORDINATE);
				mY = attributes.getValue(ATTRIBUTE_Y_COORDINATE);
				
			} else if (localName.equalsIgnoreCase(TAG_BIRD)) {
				
				mX = attributes.getValue(ATTRIBUTE_X_COORDINATE);
				mY = attributes.getValue(ATTRIBUTE_Y_COORDINATE);
				mType = attributes.getValue(ATTRIBUTE_TYPE);
				
			} else if (localName.equalsIgnoreCase(TAG_BARRIER)) {
				
				mX = attributes.getValue(ATTRIBUTE_X_COORDINATE);
				mY = attributes.getValue(ATTRIBUTE_Y_COORDINATE);
				mType = attributes.getValue(ATTRIBUTE_TYPE);
				mClass = attributes.getValue(ATTRIBUTE_CLASS);
				mRotation = attributes.getValue(ATTRIBUTE_ROTATION);
				
			} else if (localName.equalsIgnoreCase(TAG_PIG)) {
				
				mX = attributes.getValue(ATTRIBUTE_X_COORDINATE);
				mY = attributes.getValue(ATTRIBUTE_Y_COORDINATE);
				mType = attributes.getValue(ATTRIBUTE_TYPE);
				mRotation = attributes.getValue(ATTRIBUTE_ROTATION);
				
			} else if (localName.equalsIgnoreCase(TAG_NEXT_LEVEL)) {
				
				mNextWorldId = attributes.getValue(ATTRIBUTE_WORLD_ID);
				mNextStageId = attributes.getValue(ATTRIBUTE_STAGE_ID);
				mNextLevelId = attributes.getValue(ATTRIBUTE_LEVEL_ID);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			
			if (localName.equalsIgnoreCase(TAG_ARBALEST)) {
				
				mArbalest = new Arbalest(Float.parseFloat(mX), Float.parseFloat(mY));
				mX = mY = "";
				
			} else if (localName.equalsIgnoreCase(TAG_BARRIER)) {
				
				char barrierClass = 0;
				BaseBarrierObject barrier = null;
				
				if (mClass.equalsIgnoreCase(VALUE_STRAIGHT_01_CLASS)) {
					barrierClass = BarrierConstants.CLASS_STRAIGHT_01;					
				} else if (mClass.equalsIgnoreCase(VALUE_STRAIGHT_02_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_STRAIGHT_02;					
				} else if (mClass.equalsIgnoreCase(VALUE_STRAIGHT_03_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_STRAIGHT_03;					
				} else if (mClass.equalsIgnoreCase(VALUE_STRAIGHT_04_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_STRAIGHT_04;					
				} else if (mClass.equalsIgnoreCase(VALUE_SQUARE_01_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_SQUARE_01;					
				} else if (mClass.equalsIgnoreCase(VALUE_SQUARE_02_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_SQUARE_02;					
				} else if (mClass.equalsIgnoreCase(VALUE_SQUARE_03_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_SQUARE_03;					
				} else if (mClass.equalsIgnoreCase(VALUE_SQUARE_HOLLOW_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_SQUARE_HOLLOW;					
				} else if (mClass.equalsIgnoreCase(VALUE_TRIANGLE_FILLED_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_TRIANGLE_FILLED;					
				} else if (mClass.equalsIgnoreCase(VALUE_TRIANGLE_HOLLOW_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_TRIANGLE_HOLLOW;					
				} else if (mClass.equalsIgnoreCase(VALUE_CIRCLE_BIG_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_CIRCLE_BIG;					
				} else if (mClass.equalsIgnoreCase(VALUE_CIRCLE_SMALL_CLASS)) {					
					barrierClass = BarrierConstants.CLASS_CIRCLE_SMALL;					
				}
				
				if (mType.equalsIgnoreCase(VALUE_WOOD_BARRIER_TYPE)) {					
					barrier = new WoodBarrierObject(Float.parseFloat(mX), Float.parseFloat(mY), Float.parseFloat(mRotation), barrierClass);
				} else if (mType.equalsIgnoreCase(VALUE_ICE_BARRIER_TYPE)) {
					barrier = new IceBarrierObject(Float.parseFloat(mX), Float.parseFloat(mY), Float.parseFloat(mRotation), barrierClass);
				} else if (mType.equalsIgnoreCase(VALUE_STONE_BARRIER_TYPE)) {
					barrier = new StoneBarrierObject(Float.parseFloat(mX), Float.parseFloat(mY), Float.parseFloat(mRotation), barrierClass);
				}
				
				if (null != barrier) {
					mBarrierList.add(barrier);
				}
				
				mX = mY = mType = mClass = mRotation = "";
				
			} else if (localName.equalsIgnoreCase(TAG_BIRD)) {
				
				BaseBirdObject bird = null;
				
				if (mType.equalsIgnoreCase(VALUE_BLACK_BIRD_TYPE)) {
					bird = new BlackBirdObject(Float.parseFloat(mX), Float.parseFloat(mY)); 
				} else if (mType.equalsIgnoreCase(VALUE_BLUE_BIRD_TYPE)) {
					bird = new BlueBirdObject(Float.parseFloat(mX), Float.parseFloat(mY));
				} else if (mType.equalsIgnoreCase(VALUE_RED_BIRD_TYPE)) {
					bird = new RedBirdObject(Float.parseFloat(mX), Float.parseFloat(mY));
				} else if (mType.equalsIgnoreCase(VALUE_WHITE_BIRD_TYPE)) {
					bird = new WhiteBirdObject(Float.parseFloat(mX), Float.parseFloat(mY));
				} else if (mType.equalsIgnoreCase(VALUE_YELLOW_BIRD_TYPE)) {
					bird = new YellowBirdObject(Float.parseFloat(mX), Float.parseFloat(mY));
				}
				
				if (null != bird) {
					mBirdList.add(bird);
				}
				
				mX = mY = mType = "";
				
			} else if (localName.equalsIgnoreCase(TAG_PIG)) {
				
				BasePigObject pig = null;
				
				if (mType.equalsIgnoreCase(VALUE_HELMET_PIG_TYPE)) {
					pig = new HelmetPigObject(Float.parseFloat(mX), Float.parseFloat(mY), Float.parseFloat(mRotation));
				} else if (mType.equalsIgnoreCase(VALUE_KING_PIG_TYPE)) {
					pig = new KingPigObject(Float.parseFloat(mX), Float.parseFloat(mY), Float.parseFloat(mRotation));
				} else if (mType.equalsIgnoreCase(VALUE_NORMAL_PIG_TYPE)) {
					pig = new NormalPigObject(Float.parseFloat(mX), Float.parseFloat(mY), Float.parseFloat(mRotation));
				} else if (mType.equalsIgnoreCase(VALUE_OLD_PIG_TYPE)) {
					pig = new OldPigObject(Float.parseFloat(mX), Float.parseFloat(mY), Float.parseFloat(mRotation));
				} else if (mType.equalsIgnoreCase(VALUE_SMALL_PIG_TYPE)) {
					pig = new SmallPigObject(Float.parseFloat(mX), Float.parseFloat(mY), Float.parseFloat(mRotation));
				}
				
				if (null != pig) {
					mPigList.add(pig);
				}
				
				mX = mY = mRotation = "";
				
			} else if (localName.equalsIgnoreCase(TAG_NEXT_LEVEL)) {
				int worldId = Integer.parseInt(mNextWorldId);
				int stageId = Integer.parseInt(mNextStageId);
				int levelId = Integer.parseInt(mNextLevelId);
				
				if (-1 != worldId && -1 != stageId && -1 != levelId) {
					mCurrentGameLevel.setHasNext(true);
					mCurrentGameLevel.setNextLevel(new GameLevel(worldId, stageId, levelId));
				} else {
					mCurrentGameLevel.setHasNext(false);
				}
			}
		}
	}
}

